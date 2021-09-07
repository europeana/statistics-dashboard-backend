package eu.europeana.statistics.dashboard.service.utils;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsValueFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.iternal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.service.exception.FacetDeclarationFailException;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class RequestUtils {

  private RequestUtils(){}

  public static Map<MongoStatisticsField, Set<String>> parseValuesFiltersFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest){

    // Filter out Value type fields with values present
    List<MongoStatisticsField> valueField = Arrays.stream(MongoStatisticsField.values())
        .filter(value -> isValueFilterWithValues(value, statisticsFilteringRequest))
        .collect(Collectors.toList());

    return valueField.stream().collect(Collectors.toMap(field -> field,
        field -> new HashSet<>(field.getValueFilterGetter().apply(statisticsFilteringRequest).getValues())));
  }

  public static Map<MongoStatisticsField, ValueRange> parseRangeFiltersFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest){

    // Filter out Range type fields with values present
    List<MongoStatisticsField> rangeFields = Arrays.stream(MongoStatisticsField.values())
        .filter(value -> isRangeFilter(value, statisticsFilteringRequest))
        .collect(Collectors.toList());

    return rangeFields.stream().collect(Collectors.toMap(field -> field,
        field -> new ValueRange(field.getRangeFilterGetter().apply(statisticsFilteringRequest).getFrom(),
            field.getRangeFilterGetter().apply(statisticsFilteringRequest).getTo())));
  }

  public static List<MongoStatisticsField> parseBreakdownsFromRequest(
      StatisticsFilteringRequest statisticsFilteringRequest) throws FacetDeclarationFailException {

    // We want the list ordered according to the breakdown type (from 0 to 1).
    // The list order is important for later when we do the request with breakdowns
    List<StatisticsValueFilter> valueFilters = statisticsFilteringRequest.getAllValueFilters()
        .stream()
        .filter(filter -> filter.getBreakdown() != null)
        .sorted(Comparator.comparing(StatisticsValueFilter::getBreakdown))
        .collect(Collectors.toList());

    if(!isFacetDefinitionCorrect(valueFilters)){
      throw new FacetDeclarationFailException("There can only be one facet without any valued defined");
    }

    // Filter out fields that are type Value, with or without values present
    List<MongoStatisticsField> nonNullFields = Arrays.stream(MongoStatisticsField.values())
        .filter(field -> field.getValueFilterGetter() != null &&
            field.getValueFilterGetter().apply(statisticsFilteringRequest) != null).collect(Collectors.toList());

    List<MongoStatisticsField> result = new ArrayList<>();

    // To ensure the order of the list is kept the same
    for(StatisticsValueFilter filter : valueFilters){
      nonNullFields.forEach(field -> {
        if(field.getValueFilterGetter().apply(statisticsFilteringRequest).equals(filter)){
          result.add(field);
        }
      });
    }

    return result;
  }

  private static boolean isValueFilterWithValues(MongoStatisticsField mongoStatisticsField,
      StatisticsFilteringRequest statisticsFilteringRequest){
    return mongoStatisticsField.getRangeFilterGetter() == null &&
        mongoStatisticsField.getValueFilterGetter() != null &&
        mongoStatisticsField.getValueFilterGetter().apply(statisticsFilteringRequest) != null &&
        !mongoStatisticsField.getValueFilterGetter().apply(statisticsFilteringRequest).isValuesEmpty();
  }

  private static boolean isRangeFilter(MongoStatisticsField mongoStatisticsField,
      StatisticsFilteringRequest statisticsFilteringRequest){
    return mongoStatisticsField.getValueFilterGetter() == null &&
        mongoStatisticsField.getRangeFilterGetter() != null &&
        mongoStatisticsField.getRangeFilterGetter().apply(statisticsFilteringRequest) != null;
  }

  private static boolean isFacetDefinitionCorrect(List<StatisticsValueFilter> filters){
    return filters.stream().filter(filter -> filter.getBreakdown() == 0).count() == 1 &&
        filters.stream().filter(filter -> filter.getBreakdown() == 0 && filter.isValuesEmpty()).count() == 1;
  }

}
