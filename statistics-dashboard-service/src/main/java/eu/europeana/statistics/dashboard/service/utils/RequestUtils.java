package eu.europeana.statistics.dashboard.service.utils;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsBreakdownValueFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class RequestUtils {

  private RequestUtils() {
  }

  public static Map<MongoStatisticsField, Set<String>> parseValuesFiltersFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest) {

    // Filter out Value type fields
    List<MongoStatisticsField> valueField = Arrays.stream(MongoStatisticsField.values())
        .filter(value -> isValueFilter(value, statisticsFilteringRequest)).collect(Collectors.toList());

    return valueField.stream().collect(Collectors.toMap(field -> field,
        field -> new HashSet<>(field.getValueFilterGetter().apply(statisticsFilteringRequest).getValues())));
  }

  public static Map<MongoStatisticsField, ValueRange> parseRangeFiltersFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest) {

    // Filter out Range type fields with values present
    List<MongoStatisticsField> rangeFields = Arrays.stream(MongoStatisticsField.values())
        .filter(value -> isRangeFilter(value, statisticsFilteringRequest)).collect(Collectors.toList());

    return rangeFields.stream().collect(Collectors.toMap(field -> field,
        field -> new ValueRange(field.getRangeFilterGetter().apply(statisticsFilteringRequest).getFrom(),
            field.getRangeFilterGetter().apply(statisticsFilteringRequest).getTo())));
  }

  public static List<MongoStatisticsField> parseBreakdownsFromRequest(StatisticsFilteringRequest statisticsFilteringRequest)
      throws BreakdownDeclarationFailException {

    // We want the list ordered according to the breakdown value.
    // The list order is important for later when we do the request with breakdowns
    List<StatisticsBreakdownValueFilter> valueFiltersWithBreakdowns = statisticsFilteringRequest.getAllBreakdownValueFilters().stream()
        .filter(filter -> filter.getBreakdown() != null).sorted(Comparator.comparing(
            StatisticsBreakdownValueFilter::getBreakdown))
        .collect(Collectors.toList());

    if (!isBreakdownDefinitionCorrect(valueFiltersWithBreakdowns)) {
      throw new BreakdownDeclarationFailException("There are duplicate or negative breakdowns");
    }

    // Filter out fields that are type Value, with or without values present
    List<MongoStatisticsField> nonNullFields = Arrays.stream(MongoStatisticsField.values()).filter(
            field -> field.getValueFilterGetter() != null && field.getValueFilterGetter().apply(statisticsFilteringRequest) != null)
        .collect(Collectors.toList());

    List<MongoStatisticsField> result = new ArrayList<>();

    // To ensure the order of the list is kept the same
    for (StatisticsBreakdownValueFilter filter : valueFiltersWithBreakdowns) {
      nonNullFields.forEach(field -> {
        if (field.getValueFilterGetter().apply(statisticsFilteringRequest).equals(filter)) {
          result.add(field);
        }
      });
    }

    return result;
  }

  private static boolean isValueFilter(MongoStatisticsField mongoStatisticsField,
      StatisticsFilteringRequest statisticsFilteringRequest) {
    return mongoStatisticsField.getRangeFilterGetter() == null && mongoStatisticsField.getValueFilterGetter() != null
        && mongoStatisticsField.getValueFilterGetter().apply(statisticsFilteringRequest) != null;
  }

  private static boolean isRangeFilter(MongoStatisticsField mongoStatisticsField,
      StatisticsFilteringRequest statisticsFilteringRequest) {
    return mongoStatisticsField.getValueFilterGetter() == null && mongoStatisticsField.getRangeFilterGetter() != null
        && mongoStatisticsField.getRangeFilterGetter().apply(statisticsFilteringRequest) != null;
  }

  private static boolean isBreakdownDefinitionCorrect(List<StatisticsBreakdownValueFilter> filters) {

    //Make sure there are no duplicate values of the breakdown
    List<Integer> list = filters.stream().map(StatisticsBreakdownValueFilter::getBreakdown).filter(Objects::nonNull)
        .collect(Collectors.toList());
    Set<Integer> set = new HashSet<>(list);

    boolean hasNoDuplicateBreakdowns = set.size() == list.size();
    boolean hasNoNegativeBreakdowns = list.stream().noneMatch(integer -> integer < 0);
    return hasNoDuplicateBreakdowns && hasNoNegativeBreakdowns;
  }

}
