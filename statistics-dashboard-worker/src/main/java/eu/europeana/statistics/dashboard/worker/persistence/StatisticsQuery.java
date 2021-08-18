package eu.europeana.statistics.dashboard.worker.persistence;

import dev.morphia.aggregation.experimental.Aggregation;
import dev.morphia.aggregation.experimental.expressions.AccumulatorExpressions;
import dev.morphia.aggregation.experimental.expressions.Expressions;
import dev.morphia.aggregation.experimental.stages.Group;
import dev.morphia.aggregation.experimental.stages.Group.GroupId;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.filters.Filters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class represents a statistical query on the data. The class is designed so that the user can
 * execute the query multiple times.
 */
public class StatisticsQuery {

  private final static String MIN_VALUE_FIELD_NAME = "minValue";
  private final static String MAX_VALUE_FIELD_NAME = "maxValue";

  private final Supplier<Aggregation<StatisticsRecordModel>> aggregationSupplier;

  private final Map<Field, Set<String>> filterValues = new EnumMap<>(Field.class);
  private final Map<Field, ValueRange> filterRanges = new EnumMap<>(Field.class);
  private final List<Field> breakdownFields = new ArrayList<>();

  StatisticsQuery(Supplier<Aggregation<StatisticsRecordModel>> aggregationSupplier) {
    this.aggregationSupplier = aggregationSupplier;
  }

  /**
   * Add a range filter for this query. Replaces any filter set earlier for this field.
   *
   * @param field The field for which to set the range filter.
   * @param from  The lower limit (inclusive) of this range (in String ordering). Can be null, in
   *              which case no lower limit is applied.
   * @param to    The upper limit (inclusive) of this range (in String ordering). Can be null, in
   *              which case no upper limit is applied.
   * @return This instance (for chaining).
   */
  public StatisticsQuery withRangeFilter(Field field, String from, String to) {
    filterValues.remove(field);
    filterRanges.put(field, new ValueRange(from, to));
    return this;
  }

  /**
   * Add a value filter for this query. Replaces any filter set earlier for this field.
   *
   * @param field  The field for which to set the value filter.
   * @param values The values on which to filter.
   * @return This instance (for chaining).
   */
  public StatisticsQuery withValueFilter(Field field, Collection<String> values) {
    filterRanges.remove(field);
    filterValues.put(field, new HashSet<>(values));
    return this;
  }

  /**
   * Add the breakdown fields to be applied. Replaces any breakdown fields configured earlier. Note
   * that the order matters: breakdowns for fields that occur later in the list will be nested
   * inside those that occur earlier.
   *
   * @param breakdownFields The fields to apply breakdowns to. Is not null but can be empty.
   * @return This instance (for chaining).
   */
  public StatisticsQuery withBreakdowns(Field... breakdownFields) {
    this.breakdownFields.clear();
    this.breakdownFields.addAll(Arrays.asList(breakdownFields));
    return this;
  }

  private Aggregation<StatisticsRecordModel> createFilteredAggregation(Field excludeField) {

    // Compile the filters.
    final List<Filter> filters = new ArrayList<>(Field.values().length * 2);
    filterValues.forEach((field, values) -> {
      if (field != excludeField) {
        filters.add(Filters.in(field.getFieldName(), values));
      }
    });
    filterRanges.forEach((field, range) -> {
      if (field != excludeField && range.getFrom() != null) {
        filters.add(Filters.gte(field.getFieldName(), range.getFrom()));
      }
      if (field != excludeField && range.getTo() != null) {
        filters.add(Filters.lte(field.getFieldName(), range.getTo()));
      }
    });

    // Create the aggregation.
    final Aggregation<StatisticsRecordModel> pipeline = aggregationSupplier.get();
    if (!filters.isEmpty()) {
      pipeline.match(Filters.and(filters.toArray(Filter[]::new)));
    }
    return pipeline;
  }

  /**
   * Executes the query and returns the statistics data.
   *
   * @return The result of the query in the form of a statistics data tree. Is not null.
   */
  public StatisticsData queryForStatistics() {

    // Create the aggregation and add the value filters.
    final Aggregation<StatisticsRecordModel> pipeline = createFilteredAggregation(null);

    // Add the breakdowns as a grouping with a sum over the record count values.
    final GroupId groupId = Group.id();
    breakdownFields.forEach(field -> groupId.field(field.getFieldName()));
    pipeline.group(Group.group(groupId).field(StatisticsRecordModel.RECORD_COUNT_FIELD,
        AccumulatorExpressions.sum(Expressions.field(StatisticsRecordModel.RECORD_COUNT_FIELD))));

    // Execute the query
    final List<StatisticsResult> queryResults = pipeline.execute(StatisticsResult.class).toList();

    // Compile the result
    final StatisticsData result;
    if (breakdownFields.isEmpty()) {
      result = new StatisticsData(null, null, queryResults.get(0).getRecordCount());
    } else {
      result = new StatisticsData(null, null, convert(0, queryResults));
    }
    return result;
  }

  private List<StatisticsData> convert(int breakdownPosition, List<StatisticsResult> queryResults) {

    // Sanity check. We don't recurse beyond the length of the list.
    if (breakdownPosition >= breakdownFields.size()) {
      throw new IllegalStateException("Should not have recursed beyond the list length.");
    }
    final Field currentField = breakdownFields.get(breakdownPosition);

    // If we have reached the last breakdown value, we convert the results and return.
    if (breakdownPosition == breakdownFields.size() - 1) {
      return queryResults.stream().map(result -> new StatisticsData(currentField,
          result.getValue(currentField), result.getRecordCount())).collect(Collectors.toList());
    }

    // Perform the breakdown (split results by field value) and recurse.
    final Map<String, List<StatisticsResult>> breakdown = queryResults.stream()
        .collect(Collectors.groupingBy(result -> result.getValue(currentField)));
    return breakdown.entrySet().stream().map(
        entry -> new StatisticsData(currentField, entry.getKey(),
            convert(breakdownPosition + 1, entry.getValue()))).collect(Collectors.toList());
  }

  /**
   * Executes the query and return the value range for the given field. Note that any filter set
   * for this field will be ignored (i.e. the range will be taken over the data that satisfies all
   * filters except the one for this field). Also, the breakdown field settings are not relevant.
   * @param field The field for which to determine the range.
   * @return The value range that exists in the data for the given field.
   */
  public ValueRange queryForValueRange(Field field) {

    // Create the aggregation and add the value filters.
    final Aggregation<StatisticsRecordModel> pipeline = createFilteredAggregation(field);

    // Add a grouping for all data with expressions for the minimum and maximum values.
    pipeline.group(Group.group(Group.id())
        .field(MIN_VALUE_FIELD_NAME,
            AccumulatorExpressions.min(Expressions.field(field.getFieldName())))
        .field(MAX_VALUE_FIELD_NAME,
            AccumulatorExpressions.max(Expressions.field(field.getFieldName()))));

    // Execute the query.
    final List<ValueRangeResult> queryResults = pipeline.execute(ValueRangeResult.class).toList();

    // Return the result.
    if (queryResults.isEmpty()) {
      return null;
    }
    return new ValueRange(queryResults.get(0).getMinimumValue(),
        queryResults.get(0).getMaximumValue());
  }

  /**
   * Executes the query and return the value options for the given field. Note that any filter set
   * for this field will be ignored (i.e. the options will be taken from the data that satisfies all
   * filters except the one for this field). Also, the breakdown field settings are not relevant.
   * @param field The field for which to determine the options.
   * @return The value options that exists in the data for the given field.
   */
  public Set<String> queryForValueOptions(Field field) {

    // Create the aggregation and add the value filters.
    final Aggregation<StatisticsRecordModel> pipeline = createFilteredAggregation(field);

    // Add the field as a grouping so that we get the distinct values.
    pipeline.group(Group.group(Group.id(field.getFieldName())));

    // Execute the query.
    final Iterator<ValueOptionsResult> queryResults = pipeline.execute(ValueOptionsResult.class);

    // Return the result.
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(queryResults, 0), false)
        .map(ValueOptionsResult::getFieldValue).collect(Collectors.toSet());
  }

  public static class ValueRange {

    private final String from;
    private final String to;

    private ValueRange(String from, String to) {
      this.from = from;
      this.to = to;
    }

    public String getFrom() {
      return from;
    }

    public String getTo() {
      return to;
    }
  }

  /**
   * A wrapper class for the statistics query result data. The {@link Entity} annotation is needed
   * so that Morphia can handle the aggregation.
   */
  @Entity
  private static class StatisticsResult {

    @Id
    private Map<String, String> breakdownValues;

    @Property(StatisticsRecordModel.RECORD_COUNT_FIELD)
    private int recordCount;

    public Map<String, String> getBreakdownValues() {
      return breakdownValues;
    }

    public int getRecordCount() {
      return recordCount;
    }

    public String getValue(Field field) {
      return getBreakdownValues().get(field.getFieldName());
    }
  }

  /**
   * A wrapper class for the value options result data. The {@link Entity} annotation is needed so
   * that Morphia can handle the aggregation.
   */
  @Entity
  private static class ValueOptionsResult {

    @Id
    private String fieldValue;

    public String getFieldValue() {
      return fieldValue;
    }
  }

  /**
   * A wrapper class for the value range result data. The {@link Entity} annotation is needed so
   * that Morphia can handle the aggregation.
   */
  @Entity
  private static class ValueRangeResult {

    @Property(MIN_VALUE_FIELD_NAME)
    private String minimumValue;

    @Property(MAX_VALUE_FIELD_NAME)
    private String maximumValue;

    public String getMinimumValue() {
      return minimumValue;
    }

    public String getMaximumValue() {
      return maximumValue;
    }
  }
}
