package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.request.FiltersWrapper;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;
import eu.europeana.statistics.dashboard.common.api.response.BreakdownResult;
import eu.europeana.statistics.dashboard.common.api.response.FilteringOptions;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.api.response.StatisticsResult;
import eu.europeana.statistics.dashboard.common.iternal.FacetValue;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import eu.europeana.statistics.dashboard.service.utils.RequestUtils;
import eu.europeana.statistics.dashboard.common.iternal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for retrieving the statistics data that is requested
 */
@Service
public class StatisticsService {

  private static final String STATISTICS_RESULT_ROOT_VALUE = "ALL_RECORDS";
  private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("0.00");

  private final MongoSDDao mongoSDDao;

  /**
   * Constructor for the Statistics Server
   *
   * @param mongoSDDao - The mongo database it connects to
   */
  public StatisticsService(MongoSDDao mongoSDDao) {
    this.mongoSDDao = mongoSDDao;
  }

  /**
   * It queries the general data for all of Europeana
   *
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
      List<StatisticsResult> statisticsResultList = resultBreakdown.getBreakdown().stream().map(
              data -> new StatisticsResult(data.getFieldValue(), data.getRecordCount(),
                  calculatePercentage(totalRecordCount, data.getRecordCount())))
          .collect(Collectors.toList());

      allBreakdownsEuropeana.add(new BreakdownResult(breakdownBy, statisticsResultList));

    }
    return new ResultListFilters(allBreakdownsEuropeana);
  }

  /**
   * It queries the requested data taking into account the filters
   *
   * @param statisticsRequest The wrapper that contains the datasetId and the filters and its
   * respective values to query
   * @return An object containing the result of the filtering and the available options based on the
   * filtering performed
   */
  public FilteringResult queryDataWithFilters(FiltersWrapper statisticsRequest)
      throws BreakdownDeclarationFailException {
    //Prepares the query with the requested filters
    StatisticsQuery readyQuery = prepareFilteringQuery(statisticsRequest);

    //Executes the query and obtains the result
    StatisticsData queryResult = readyQuery.queryForStatistics();

    // The available options are based on the query that was executed previously
    Map<MongoStatisticsField, Set<String>> valueAvailableOptions = prepareValueFilteringOptionsQuery(
        readyQuery);
    Map<MongoStatisticsField, Optional<ValueRange>> rangeAvailableOptions = prepareRangeFilteringOptionsQuery(
        readyQuery);

    int totalRecords = queryResult.getRecordCount();

    StatisticsResult statisticsAllRecordsResult = new StatisticsResult(STATISTICS_RESULT_ROOT_VALUE,
        totalRecords, calculatePercentage(totalRecords, totalRecords));

    // If there are breakdowns present, parse them and set them in the final result
    if (!queryResult.isBreakdownListEmpty()) {
      Pair<FacetValue, List<StatisticsResult>> parsedBreakdown = parseBreakdownsAsOutput(
          queryResult, totalRecords);
      statisticsAllRecordsResult.setBreakdowns(
          new BreakdownResult(parsedBreakdown.getKey(), parsedBreakdown.getValue()));
    }

    // Converting the result of available options to object FilteringOptions
    FilteringOptions filteringOptions = new FilteringOptions();
    valueAvailableOptions.forEach(
        (key, value) -> key.getValueFilterSetter().accept(filteringOptions, value));
    rangeAvailableOptions.forEach((key, value) -> key.getRangeFilterSetter()
        .accept(filteringOptions,
            new StatisticsRangeFilter(value.map(ValueRange::getFrom).orElse(null),
                value.map(ValueRange::getTo).orElse(null))));

    return new FilteringResult(statisticsAllRecordsResult, filteringOptions);
  }

  private List<StatisticsData> prepareGeneralQueries() {
    StatisticsQuery query = mongoSDDao.createStatisticsQuery();
    List<StatisticsData> queries = new ArrayList<>();

    // Get all Value fields type
    List<MongoStatisticsField> filterMongoStatisticFields = Arrays.stream(
        MongoStatisticsField.values()).filter(
        field -> field != MongoStatisticsField.UPDATED_DATE
            && field != MongoStatisticsField.CREATED_DATE
            && field != MongoStatisticsField.DATASET_ID).collect(Collectors.toUnmodifiableList());

    // Execute a breakdown query for each Value field
    filterMongoStatisticFields.forEach(
        field -> queries.add(query.withBreakdowns(field).queryForStatistics()));

    return queries;
  }

  private StatisticsQuery prepareFilteringQuery(FiltersWrapper statisticsRequest)
      throws BreakdownDeclarationFailException {
    StatisticsFilteringRequest filters = statisticsRequest.getFilters();
    StatisticsQuery query = mongoSDDao.createStatisticsQuery();

    Map<MongoStatisticsField, Set<String>> parsedValueFilters = RequestUtils.parseValuesFiltersFromRequest(
        filters);
    Map<MongoStatisticsField, ValueRange> parsedRangeFilters = RequestUtils.parseRangeFiltersFromRequest(
        filters);
    List<MongoStatisticsField> breakdowns = RequestUtils.parseBreakdownsFromRequest(filters);

    // Set each parsed value into query
    parsedValueFilters.forEach(query::withValueFilter);
    parsedRangeFilters.forEach(
        (key, value) -> query.withRangeFilter(key, value.getFrom(), value.getTo()));
    query.withBreakdowns(breakdowns.toArray(MongoStatisticsField[]::new));

    return query;

  }

  private Map<MongoStatisticsField, Set<String>> prepareValueFilteringOptionsQuery(
      StatisticsQuery query) {
    Map<MongoStatisticsField, Set<String>> result = new EnumMap<>(MongoStatisticsField.class);
    MongoStatisticsField.getValueFields()
        .forEach(field -> result.put(field, query.queryForValueOptions(field)));
    return result;

  }

  private Map<MongoStatisticsField, Optional<ValueRange>> prepareRangeFilteringOptionsQuery(
      StatisticsQuery query) {
    Map<MongoStatisticsField, Optional<ValueRange>> result = new EnumMap<>(
        MongoStatisticsField.class);
    MongoStatisticsField.getRangeFields()
        .forEach(field -> result.put(field,
            Optional.ofNullable(query.queryForValueRange(field)).orElse(null)));
    return result;

  }

  private Pair<FacetValue, List<StatisticsResult>> parseBreakdownsAsOutput(
      StatisticsData queryResult, int totalRecords) {

    // Get Facet for the following breakdown
    FacetValue breakdownBy = queryResult.getBreakdown().get(0).getField().getFacet();
    List<StatisticsResult> breakdownsResults = new ArrayList<>();

    // For each Facet value, set up the value, record count and percentage
    for (StatisticsData breakdown : queryResult.getBreakdown()) {
      StatisticsResult newElement = new StatisticsResult(breakdown.getFieldValue(),
          breakdown.getRecordCount(),
          calculatePercentage(totalRecords, breakdown.getRecordCount()));

      // If Facet value contains breakdown, apply the same steps as before
      if (!breakdown.isBreakdownListEmpty()) {
        List<StatisticsResult> newListBreakdownsResult = new ArrayList<>();
        FacetValue newFacetBreakdownValue = breakdown.getBreakdown().get(0).getField().getFacet();
        breakdown.getBreakdown().forEach(value -> newListBreakdownsResult.add(
            new StatisticsResult(value.getFieldValue(), value.getRecordCount(),
                calculatePercentage(totalRecords, value.getRecordCount()))));
        newElement.setBreakdowns(
            new BreakdownResult(newFacetBreakdownValue, newListBreakdownsResult));
      }

      breakdownsResults.add(newElement);
    }

    return new ImmutablePair<>(breakdownBy, breakdownsResults);
  }

  private double calculatePercentage(double totalCount, double count) {
    if (totalCount <= 0) {
      return 0;
    }
    return Double.parseDouble(PERCENTAGE_FORMAT.format((count / totalCount) * 100.0));
  }

}
