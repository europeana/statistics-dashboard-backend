package eu.europeana.statistics.dashboard.service.utils;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsValueFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.iternal.FieldMongoStatistics;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestUtils {

  public static Map<FieldMongoStatistics, Set<String>> parseValuesFiltersFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest){

    List<FieldMongoStatistics> valueField = Arrays.stream(FieldMongoStatistics.values())
        .filter(value -> isValueFilterWithValues(value, statisticsFilteringRequest))
        .collect(Collectors.toList());

    Map<FieldMongoStatistics, Set<String>> result = new HashMap<>();

    valueField.forEach(field -> result.put(field,
        new HashSet<>(field.getValueFilterGetter().apply(statisticsFilteringRequest).getValues())));

    return result;
  }

  public static Map<FieldMongoStatistics, ValueRange> parseRangeFiltersFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest){

    List<FieldMongoStatistics> rangeFields = Arrays.stream(FieldMongoStatistics.values())
        .filter(value -> isRangeFilter(value, statisticsFilteringRequest))
        .collect(Collectors.toList());

    Map<FieldMongoStatistics, ValueRange> result = new HashMap<>();

    rangeFields.forEach(field -> result.put(field, new ValueRange(field.getRangeFilterGetter().apply(statisticsFilteringRequest).getFrom(),
        field.getRangeFilterGetter().apply(statisticsFilteringRequest).getTo())));

    return result;
  }

  public static List<FieldMongoStatistics> parseBreakdownsFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest){

    // We want the list ordered according to the breakdown type (from 0 to 1).
    // The list order is important for later when we do the request with breakdowns
    List<StatisticsValueFilter> countFilters = statisticsFilteringRequest.getAllCountFilters()
        .stream()
        .filter(filter -> filter.getBreakdown() != null)
        .sorted(Comparator.comparing(StatisticsValueFilter::getBreakdown))
        .collect(Collectors.toList());

    List<FieldMongoStatistics> nonNullFilters = Arrays.stream(FieldMongoStatistics.values())
        .filter(field -> field.getValueFilterGetter() != null &&
            field.getValueFilterGetter().apply(statisticsFilteringRequest) != null).collect(Collectors.toList());

    List<FieldMongoStatistics> result = new ArrayList<>();

    // To ensure the order of the list is kept the same
    for(StatisticsValueFilter filter : countFilters){
      nonNullFilters.forEach(field -> {
        if(field.getValueFilterGetter().apply(statisticsFilteringRequest).equals(filter)){
          result.add(field);
        }
      });
    }

    return result;
  }

  private static boolean isValueFilterWithValues(FieldMongoStatistics fieldMongoStatistics,
      StatisticsFilteringRequest statisticsFilteringRequest){
    return fieldMongoStatistics.getRangeFilterGetter() == null &&
        fieldMongoStatistics.getValueFilterGetter() != null &&
        fieldMongoStatistics.getValueFilterGetter().apply(statisticsFilteringRequest) != null &&
        !fieldMongoStatistics.getValueFilterGetter().apply(statisticsFilteringRequest).isValuesEmpty();
  }

  private static boolean isRangeFilter(FieldMongoStatistics fieldMongoStatistics,
      StatisticsFilteringRequest statisticsFilteringRequest){
    return fieldMongoStatistics.getValueFilterGetter() == null &&
        fieldMongoStatistics.getRangeFilterGetter() != null &&
        fieldMongoStatistics.getRangeFilterGetter().apply(statisticsFilteringRequest) != null;
  }

}
