package eu.europeana.statistics.dashboard.service.persistence;

import static eu.europeana.metis.network.ExternalRequestUtil.retryableExternalRequestForNetworkExceptions;

import dev.morphia.aggregation.Aggregation;
import dev.morphia.aggregation.expressions.AccumulatorExpressions;
import dev.morphia.aggregation.expressions.Expressions;
import dev.morphia.aggregation.stages.Group;
import dev.morphia.aggregation.stages.Group.GroupId;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import eu.europeana.metis.mongo.utils.MorphiaUtils;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.common.internal.model.StatisticsRecordModel;
import eu.europeana.statistics.dashboard.common.utils.MongoFieldNames;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class represents a statistical query on the data. The class is designed so that the user can execute the query multiple
 * times.
 */
public class StatisticsQuery {

  private static final String MIN_VALUE_FIELD_NAME = "minValue";
  private static final String MAX_VALUE_FIELD_NAME = "maxValue";

  private final Supplier<Aggregation<StatisticsRecordModel>> aggregationSupplier;

  private final Map<MongoStatisticsField, Set<String>> filterValues = new EnumMap<>(MongoStatisticsField.class);
  private final Map<MongoStatisticsField, ValueRange> filterRanges = new EnumMap<>(MongoStatisticsField.class);
  private final List<MongoStatisticsField> breakdownMongoStatisticFields = new ArrayList<>();

  /**
   * Instantiates a new Statistics query.
   *
   * @param aggregationSupplier the aggregation supplier
   */
  StatisticsQuery(Supplier<Aggregation<StatisticsRecordModel>> aggregationSupplier) {
    this.aggregationSupplier = aggregationSupplier;
  }

  /**
   * Add a range filter for this query. Replaces any filter set earlier for this field.
   *
   * @param mongoStatisticsField The field for which to set the range filter.
   * @param from                 The lower limit (inclusive) of this range (in String ordering). Can be null, in which case no lower limit is applied.
   * @param to                   The upper limit (inclusive) of this range (in String ordering). Can be null, in which case no upper limit is applied.
   * @return This instance (for chaining).
   */
  public StatisticsQuery withRangeFilter(MongoStatisticsField mongoStatisticsField, String from, String to) {
    filterValues.remove(mongoStatisticsField);
    filterRanges.put(mongoStatisticsField, new ValueRange(from, to));
    return this;
  }

  /**
   * Add a value filter for this query. Replaces any filter set earlier for this field.
   *
   * @param field  The field for which to set the value filter.
   * @param values The values on which to filter.
   * @return This instance (for chaining).
   */
  public StatisticsQuery withValueFilter(MongoStatisticsField field, Collection<String> values) {
    filterRanges.remove(field);
    filterValues.put(field, new HashSet<>(values));
    return this;
  }

  /**
   * Add the breakdown fields to be applied. Replaces any breakdown fields configured earlier. Note that the order matters:
   * breakdowns for fields that occur later in the list will be nested inside those that occur earlier.
   *
   * @param breakdownField The fields to apply breakdowns to. Is not null but can be empty.
   * @return This instance (for chaining).
   */
  public StatisticsQuery withBreakdowns(MongoStatisticsField... breakdownField) {
    this.breakdownMongoStatisticFields.clear();
    this.breakdownMongoStatisticFields.addAll(Arrays.asList(breakdownField));
    return this;
  }

  private Aggregation<StatisticsRecordModel> createFilteredAggregation(MongoStatisticsField excludeMongoStatisticsField) {

    // Compile the filters.
    final List<Filter> filters = new ArrayList<>(MongoStatisticsField.values().length * 2);
    filterValues.forEach((field, values) -> {
      if (field != excludeMongoStatisticsField) {
        final Filter filter;
        if (values.isEmpty()) {
          filter = Filters.exists(field.getFieldName());
        } else {
          filter = Filters.in(field.getFieldName(), values);
        }
        filters.add(filter);
      }
    });
    filterRanges.forEach((field, range) -> {
      if (field != excludeMongoStatisticsField && range.getFrom() != null) {
        filters.add(Filters.gte(field.getFieldName(), range.getFrom()));
      }
      if (field != excludeMongoStatisticsField && range.getTo() != null) {
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
    final Aggregation<StatisticsRecordModel> aggregation = createFilteredAggregation(null);

    // Add the breakdowns as a grouping with a sum over the record count values.
    final GroupId groupId = Group.id();
    breakdownMongoStatisticFields.forEach(field -> groupId.field(field.getFieldName()));
    aggregation.group(Group.group(groupId).field(MongoFieldNames.RECORD_COUNT_FIELD,
        AccumulatorExpressions.sum(Expressions.field(MongoFieldNames.RECORD_COUNT_FIELD))));

    // Execute the query
    final List<StatisticsResult> queryResults = MorphiaUtils.getListOfAggregationRetryable(aggregation, StatisticsResult.class);

    if(queryResults.isEmpty()){
      return new StatisticsData(null, null, 0);
    }

    // Compile the result
    final StatisticsData result;
    if (breakdownMongoStatisticFields.isEmpty()) {
      result = new StatisticsData(null, null, queryResults.getFirst().getRecordCount());
    } else {
      result = new StatisticsData(null, null, convert(0, queryResults));
    }
    return result;
  }

  private List<StatisticsData> convert(int breakdownPosition, List<StatisticsResult> queryResults) {

    // Sanity check. We don't recurse beyond the length of the list.
    if (breakdownPosition >= breakdownMongoStatisticFields.size()) {
      throw new IllegalStateException("Should not have recursed beyond the list length.");
    }
    final MongoStatisticsField currentMongoStatisticsField = breakdownMongoStatisticFields.get(breakdownPosition);

    // If we have reached the last breakdown value, we convert the results and return.
    if (breakdownPosition == breakdownMongoStatisticFields.size() - 1) {
      return queryResults.stream().map(
          result -> new StatisticsData(currentMongoStatisticsField, result.getValue(currentMongoStatisticsField),
              result.getRecordCount())).collect(Collectors.toList());
    }

    // Perform the breakdown (split results by field value) and recurse.
    final Map<String, List<StatisticsResult>> breakdown = queryResults.stream()
        .collect(Collectors.groupingBy(result -> result.getValue(currentMongoStatisticsField)));
    return breakdown.entrySet().stream().map(entry -> new StatisticsData(currentMongoStatisticsField, entry.getKey(),
        convert(breakdownPosition + 1, entry.getValue()))).collect(Collectors.toList());
  }

  /**
   * Executes the query and return the value range for the given field. Note that any filter set for this field will be ignored
   * (i.e. the range will be taken over the data that satisfies all filters except the one for this field). Also, the breakdown
   * field settings are not relevant.
   *
   * @param mongoStatisticsField The field for which to determine the range.
   * @return The value range that exists in the data for the given field in a non-null {@link Optional}
   */
  public Optional<ValueRange> queryForValueRange(MongoStatisticsField mongoStatisticsField) {

    // Create the aggregation and add the value filters.
    final Aggregation<StatisticsRecordModel> aggregation = createFilteredAggregation(mongoStatisticsField);

    // Add a grouping for all data with expressions for the minimum and maximum values.
    aggregation.group(Group.group(Group.id())
        .field(MIN_VALUE_FIELD_NAME, AccumulatorExpressions.min(Expressions.field(mongoStatisticsField.getFieldName())))
        .field(MAX_VALUE_FIELD_NAME, AccumulatorExpressions.max(Expressions.field(mongoStatisticsField.getFieldName()))));

    // Execute the query.
    final List<ValueRangeResult> queryResults = MorphiaUtils.getListOfAggregationRetryable(aggregation, ValueRangeResult.class);

    // Return the result.
    if (queryResults.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new ValueRange(queryResults.getFirst().getMinimumValue(), queryResults.getFirst().getMaximumValue()));
  }

  /**
   * Executes the query and return the value options for the given field. Note that any filter set for this field will be ignored
   * (i.e. the options will be taken from the data that satisfies all filters except the one for this field). Also, the breakdown
   * field settings are not relevant.
   *
   * @param mongoStatisticsField The field for which to determine the options.
   * @return The value options that exists in the data for the given field.
   */
  public Set<String> queryForValueOptions(MongoStatisticsField mongoStatisticsField) {

    // Create the aggregation and add the value filters.
    final Aggregation<StatisticsRecordModel> pipeline = createFilteredAggregation(mongoStatisticsField);

    // Add the field as a grouping so that we get the distinct values.
    pipeline.group(Group.group(Group.id(mongoStatisticsField.getFieldName())));

    // Execute the query.
    final Iterator<ValueOptionsResult> queryResults = retryableExternalRequestForNetworkExceptions(
        () -> pipeline.execute(ValueOptionsResult.class));

    // Return the result.
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(queryResults, 0), false)
        .map(ValueOptionsResult::getFieldValue).collect(Collectors.toSet());
  }

  /**
   * The type Value range.
   */
  public static class ValueRange {

    private final String from;
    private final String to;

    /**
     * Instantiates a new Value range.
     *
     * @param from the from
     * @param to   the to
     */
    public ValueRange(String from, String to) {
      this.from = from;
      this.to = to;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public String getFrom() {
      return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public String getTo() {
      return to;
    }
  }

  /**
   * A wrapper class for the statistics query result data. The {@link Entity} annotation is needed so that Morphia can handle the
   * aggregation.
   */
  @Entity
  private static class StatisticsResult {

    @Id
    private Map<String, String> breakdownValues;

    @Property(MongoFieldNames.RECORD_COUNT_FIELD)
    private int recordCount;

    /**
     * Gets breakdown values.
     *
     * @return the breakdown values
     */
    public Map<String, String> getBreakdownValues() {
      return Collections.unmodifiableMap(breakdownValues);
    }

    /**
     * Gets record count.
     *
     * @return the record count
     */
    public int getRecordCount() {
      return recordCount;
    }

    /**
     * Gets value.
     *
     * @param mongoStatisticsField the mongo statistics field
     * @return the value
     */
    public String getValue(MongoStatisticsField mongoStatisticsField) {
      return getBreakdownValues().get(mongoStatisticsField.getFieldName());
    }
  }

  /**
   * A wrapper class for the value options result data. The {@link Entity} annotation is needed so that Morphia can handle the
   * aggregation.
   */
  @Entity
  private static class ValueOptionsResult {

    @Id
    private String fieldValue;

    /**
     * Gets field value.
     *
     * @return the field value
     */
    public String getFieldValue() {
      return fieldValue;
    }
  }

  /**
   * A wrapper class for the value range result data. The {@link Entity} annotation is needed so that Morphia can handle the
   * aggregation.
   */
  @Entity
  private static class ValueRangeResult {

    @Property(MIN_VALUE_FIELD_NAME)
    private String minimumValue;

    @Property(MAX_VALUE_FIELD_NAME)
    private String maximumValue;

    /**
     * Gets minimum value.
     *
     * @return the minimum value
     */
    public String getMinimumValue() {
      return minimumValue;
    }

    /**
     * Gets maximum value.
     *
     * @return the maximum value
     */
    public String getMaximumValue() {
      return maximumValue;
    }
  }
}
