package eu.europeana.statistics.dashboard.service.server;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;
import eu.europeana.statistics.dashboard.common.api.response.BreakdownResult;
import eu.europeana.statistics.dashboard.common.api.response.FilteringOptions;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.api.response.StatisticsResult;
import eu.europeana.statistics.dashboard.common.iternal.FacetValue;
import eu.europeana.statistics.dashboard.service.utils.RequestUtils;
import eu.europeana.statistics.dashboard.common.iternal.FieldMongoStatistics;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for retrieving the statistics data that is requested
 */
@Component
public class StatisticsServer {

  private final MongoSDDao mongoSDDao;

  /**
   * Constructor for the Statistics Server
   *
   * @param mongoSDDao - The mongo database it connects to
   */
  public StatisticsServer(MongoSDDao mongoSDDao) {
    this.mongoSDDao = mongoSDDao;
  }

  /**
   * It queries the general data for all of Europeana
   * @return A list with all breakdowns and their respective value
   */
  public ResultListFilters queryGeneralEuropeanaData() {
    // It returns a list of StatisticsData, where each has a list of breakdowns
    List<StatisticsData> generalQueries = prepareGeneralQueries();
    // Get total count of records (it is the same for each group of breakdowns)
    int totalRecordCount = generalQueries.get(0).getRecordCount();

    List<BreakdownResult> allBreakdownsEuropeana = new ArrayList<>();
    for (StatisticsData resultBreakdown : generalQueries) {
      // Replace Field with FacetValue object
      FacetValue breakdownBy = resultBreakdown.getBreakdown().get(0).getField().getFacet();

      // Convert StatisticsData into a list of StatisticResult
      List<StatisticsResult> statisticsResultList = resultBreakdown.getBreakdown().stream()
          .map(data -> new StatisticsResult(data.getFieldValue(), data.getRecordCount(),
              getPercentage(totalRecordCount, data.getRecordCount())))
          .collect(Collectors.toList());

      allBreakdownsEuropeana.add(new BreakdownResult(breakdownBy, statisticsResultList));

    }
    return new ResultListFilters(allBreakdownsEuropeana);
  }

  /**
   * It queries the requested data taking into account the filters
   * @param statisticsRequest The filters and its respective values to query
   * @return An object containing the result of the filtering and the available
   *    * options based on the filtering performed
   */
  public FilteringResult queryDataWithFilters(StatisticsFilteringRequest statisticsRequest) {
    //Prepares the query with the requested filters
    StatisticsQuery readyQuery = prepareFilteringQuery(statisticsRequest);

    //Executed the query and obtains the result
    StatisticsData queryResult = readyQuery.queryForStatistics();

    // The available options are based on the query that was executed previously
    Map<FieldMongoStatistics, Set<String>> valueAvailableOptions = prepareValueFilteringOptionsQuery(readyQuery);
    Map<FieldMongoStatistics, ValueRange> rangeAvailableOptions = prepareRangeFilteringOptionsQuery(readyQuery);

    int totalRecords = queryResult.getRecordCount();
    StatisticsResult statisticsAllRecordsResult = new StatisticsResult("ALL_RECORDS", totalRecords, 100);

    if(!queryResult.isBreakdownListEmpty()) {
      Pair<FacetValue, List<StatisticsResult>> breakdownParsing = parseBreakdownsAsOutput(queryResult, totalRecords);
      statisticsAllRecordsResult.setBreakdowns(new BreakdownResult(breakdownParsing.getKey(),breakdownParsing.getValue()));
    }

    // Converting the result of available options to object FilteringOptions
    FilteringOptions filteringOptions = new FilteringOptions();
    valueAvailableOptions.forEach((key, value) -> key.getValueFilterSetter().accept(filteringOptions, value));
    rangeAvailableOptions.forEach((key, value) -> key.getRangeFilterSetter()
        .accept(filteringOptions, new StatisticsRangeFilter(value.getFrom(), value.getTo())));

    return new FilteringResult(statisticsAllRecordsResult, filteringOptions);
  }

  private List<StatisticsData> prepareGeneralQueries() {
    StatisticsQuery query = mongoSDDao.createStatisticsQuery();
    List<StatisticsData> queries = new ArrayList<>();

    // Get all Value fields type
    List<FieldMongoStatistics> filterFieldMongoStatistics = Arrays.stream(FieldMongoStatistics.values())
        .filter(field -> field != FieldMongoStatistics.UPDATED_DATE &&
            field != FieldMongoStatistics.CREATED_DATE &&
            field != FieldMongoStatistics.DATASET_ID)
        .collect(Collectors.toUnmodifiableList());

    // Execute a breakdown query for each Value field
    filterFieldMongoStatistics.forEach(field -> queries.add(query.withBreakdowns(field).queryForStatistics()));

    return queries;
  }

  private StatisticsQuery prepareFilteringQuery(StatisticsFilteringRequest statisticsRequest){
    StatisticsQuery query = mongoSDDao.createStatisticsQuery();

    Map<FieldMongoStatistics, Set<String>> parsedValueFilters = RequestUtils
        .parseValuesFiltersFromRequest(statisticsRequest);
    Map<FieldMongoStatistics, ValueRange> parsedRangeFilters = RequestUtils
        .parseRangeFiltersFromRequest(statisticsRequest);
    List<FieldMongoStatistics> breakdowns = RequestUtils
        .parseBreakdownsFromRequest(statisticsRequest);

    parsedValueFilters.forEach(query::withValueFilter);
    parsedRangeFilters.forEach(
        (key, value) -> query.withRangeFilter(key, value.getFrom(), value.getTo()));
    query.withBreakdowns(breakdowns.toArray(FieldMongoStatistics[]::new));

    return query;

  }

  private Map<FieldMongoStatistics, Set<String>> prepareValueFilteringOptionsQuery(StatisticsQuery query){
    Map<FieldMongoStatistics, Set<String>> result = new HashMap<>();
    FieldMongoStatistics.getValueFields().forEach(field -> result.put(field, query.queryForValueOptions(field)));
    return result;

  }

  private Map<FieldMongoStatistics, ValueRange> prepareRangeFilteringOptionsQuery(StatisticsQuery query){
    Map<FieldMongoStatistics, ValueRange> result = new HashMap<>();
    FieldMongoStatistics.getRangeFields().forEach(field -> result.put(field, query.queryForValueRange(field)));
    return result;

  }

  private Pair<FacetValue, List<StatisticsResult>> parseBreakdownsAsOutput(StatisticsData queryResult, int totalRecords){

    FacetValue breakdownBy = queryResult.getBreakdown().get(0).getField().getFacet();
    List<StatisticsResult> breakdownsResults = new ArrayList<>();

    for (StatisticsData breakdown : queryResult.getBreakdown()) {
      StatisticsResult newElement = new StatisticsResult(breakdown.getFieldValue(),
          breakdown.getRecordCount(), getPercentage(totalRecords, breakdown.getRecordCount()));

      if (!breakdown.isBreakdownListEmpty()) {
        List<StatisticsResult> newListBreakdownsResult = new ArrayList<>();
        FacetValue newFacetBreakdownValue = breakdown.getBreakdown().get(0).getField().getFacet();
        breakdown.getBreakdown().forEach(value ->
            newListBreakdownsResult.add(new StatisticsResult(value.getFieldValue(),
                value.getRecordCount(), getPercentage(totalRecords, value.getRecordCount())))
        );
        newElement.setBreakdowns(new BreakdownResult(newFacetBreakdownValue, newListBreakdownsResult));
      }

      breakdownsResults.add(newElement);
    }

    return new ImmutablePair<>(breakdownBy, breakdownsResults);
  }

  private double getPercentage(double totalCount, double count) {
    return (count / totalCount) * 100;
  }

}
