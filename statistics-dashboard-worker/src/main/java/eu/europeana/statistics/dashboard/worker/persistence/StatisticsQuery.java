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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class represents a statistical query on the data.
 */
public class StatisticsQuery {

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

  /**
   * Executes the query and returns the result.
   *
   * @return The result of the query in the form of a statistics data tree. Is not null.
   */
  public StatisticsData perform() {

    // Create the aggregation.
    final Aggregation<StatisticsRecordModel> pipeline = aggregationSupplier.get();

    // Add the value filters
    final List<Filter> filters = new ArrayList<>(Field.values().length * 2);
    filterValues.forEach((field, values) -> filters.add(Filters.in(field.getFieldName(), values)));
    filterRanges.forEach((field, range) -> {
      if (range.getFrom() != null) {
        filters.add(Filters.gte(field.getFieldName(), range.getFrom()));
      }
      if (range.getTo() != null) {
        filters.add(Filters.lte(field.getFieldName(), range.getTo()));
      }
    });
    if (!filters.isEmpty()) {
      pipeline.match(Filters.and(filters.toArray(Filter[]::new)));
    }

    // Add the breakdowns as a grouping with a sum over the
    final GroupId groupId = Group.id();
    breakdownFields.forEach(field -> groupId.field(field.getFieldName()));
    pipeline.group(Group.group(groupId).field(StatisticsRecordModel.RECORD_COUNT_FIELD,
        AccumulatorExpressions.sum(Expressions.field(StatisticsRecordModel.RECORD_COUNT_FIELD))));

    // Execute the query
    final List<QueryResult> queryResults = pipeline.execute(QueryResult.class).toList();

    // Compile the result
    final StatisticsData result;
    if (breakdownFields.isEmpty()) {
      result = new StatisticsData(null, null, queryResults.get(0).getRecordCount());
    } else {
      result = new StatisticsData(null, null, convert(0, queryResults));
    }
    return result;
  }

  private List<StatisticsData> convert(int breakdownPosition, List<QueryResult> queryResults) {

    // Sanity check. We don't recurse beyond the length of the list.
    if (breakdownPosition >= breakdownFields.size()) {
      throw new IllegalStateException("Should not have recursed beyond the list length.");
    }
    final Field currentField = breakdownFields.get(breakdownPosition);

    // If we have reached the last breakdown value, we convert the results and return.
    if (breakdownPosition == breakdownFields.size() - 1) {
      return queryResults.stream().map(
          queryResult -> new StatisticsData(currentField, queryResult.getValue(currentField),
              queryResult.getRecordCount())).collect(Collectors.toList());
    }

    // Perform the breakdown (split results by field value) and recurse.
    final Map<String, List<QueryResult>> breakdown = queryResults.stream()
        .collect(Collectors.groupingBy(queryResult -> queryResult.getValue(currentField)));
    return breakdown.entrySet().stream().map(
        entry -> new StatisticsData(currentField, entry.getKey(),
            convert(breakdownPosition + 1, entry.getValue()))).collect(Collectors.toList());
  }

  private static class ValueRange {

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
   * A wrapper class for the query result data. The {@link Entity} annotation is needed so that
   * Morphia can handle the aggregation.
   */
  @Entity
  private static class QueryResult {

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
}
