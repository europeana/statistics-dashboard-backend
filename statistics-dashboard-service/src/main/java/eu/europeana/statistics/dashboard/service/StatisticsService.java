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
import eu.europeana.statistics.dashboard.common.iternal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import eu.europeana.statistics.dashboard.service.utils.RequestUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for retrieving the statistics data that is requested
 */
@Service
public class StatisticsService {

  private static final String STATISTICS_RESULT_ROOT_VALUE = "ALL_RECORDS";
  private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
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
   * It queries the general data for all of Europeana without content Tier 0
   *
   * @return A list with all breakdowns and their respective value
   */
  public ResultListFilters queryGeneralEuropeanaDataWithoutContentTierZero() {
    // Get total count of records (it is the same for each group of breakdowns)
    return getResultListFilters(
        // It returns a list of StatisticsData, where each has a list of breakdowns
        prepareGeneralQueriesWithoutContentTierZero(getStatisticsQuery(),
            getMongoStatisticFields())
    );
  }

  /**
   * It queries the general data for all of Europeana Including Content Tier 0
   *
   * @return A list with all breakdowns and their respective value
   */
  public ResultListFilters queryGeneralEuropeanaDataIncludingContentTierZero() {
    // Get total count of records (it is the same for each group of breakdowns)
    return getResultListFilters(
        // It returns a list of StatisticsData, where each has a list of breakdowns
        prepareGeneralQueries(getStatisticsQuery(), getMongoStatisticFields()
        )
    );
  }

  /**
   * Build the results list filters according to breakdowns and calculates its percentages from total records count
   *
   * @param statisticsDataList list of all breakdowns
   * @return ResultListFilters
   */
  @NotNull
  private ResultListFilters getResultListFilters(List<StatisticsData> statisticsDataList) {
    return new ResultListFilters(calculateBreakDownsResultList(statisticsDataList));
  }

  /**
   * Calculate all breakdowns Europeana
   *
   * @param allBreakdowns List of statistics data
   * @return List of BreakdownResult
   */
  private List<BreakdownResult> calculateBreakDownsResultList(List<StatisticsData> allBreakdowns) {
    int totalRecordCount = allBreakdowns.get(0).getRecordCount();
    return allBreakdowns
        .stream()
        .map(statisticsData -> {
          // Replace Field with FacetValue object
          FacetValue breakdownBy = statisticsData.getBreakdown()
                                                 .get(0)
                                                 .getField()
                                                 .getFacet();

          return new BreakdownResult(breakdownBy,
              calculateBreakDownStatisticsResult(statisticsData, totalRecordCount)
          );
        })
        .collect(Collectors.toList());
  }

  /**
   * Calculates the percentage from StatisticsData for every breakdown
   *
   * @param statisticsData data
   * @param totalRecordCount total records
   * @return List of StatisticsResult
   */
  private List<StatisticsResult> calculateBreakDownStatisticsResult(StatisticsData statisticsData, int totalRecordCount) {
    return statisticsData.getBreakdown()
                         .stream()
                         .map(breakdown -> new StatisticsResult(
                             breakdown.getFieldValue(),
                             breakdown.getRecordCount(),
                             calculatePercentage(
                                 totalRecordCount,
                                 breakdown.getRecordCount())))
                         .collect(Collectors.toList());
  }

  /**
   * It queries the requested data taking into account the filters
   *
   * @param statisticsRequest The wrapper that contains the datasetId and the filters and its respective values to query
   * @return An object containing the result of the filtering and the available options based on the filtering performed
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

  private List<StatisticsData> prepareGeneralQueries(StatisticsQuery query,
      List<MongoStatisticsField> filterMongoStatisticFields) {
    // Execute a breakdown query for each Value field
    return filterMongoStatisticFields.stream()
                                     .map(field -> query.withBreakdowns(field)
                                                        .queryForStatistics())
                                     .collect(Collectors.toList());
  }

  private List<StatisticsData> prepareGeneralQueriesWithoutContentTierZero(StatisticsQuery query,
      List<MongoStatisticsField> filterMongoStatisticFields) {
    // Execute a breakdown query for each Value field
    return filterMongoStatisticFields.stream()
                                     .map(field -> query.withBreakdowns(field)
                                                        .withValueFilter(MongoStatisticsField.CONTENT_TIER,
                                                            List.of("1", "2", "3", "4"))
                                                        .queryForStatistics())
                                     .collect(Collectors.toList());
  }

  @NotNull
  private List<MongoStatisticsField> getMongoStatisticFields() {
    return Arrays.stream(
        MongoStatisticsField.values()).filter(
        field -> field != MongoStatisticsField.UPDATED_DATE
            && field != MongoStatisticsField.CREATED_DATE
            && field != MongoStatisticsField.DATASET_ID).collect(Collectors.toUnmodifiableList());
  }

  private StatisticsQuery getStatisticsQuery() {
    return mongoSDDao.createStatisticsQuery();
  }

  private StatisticsQuery prepareFilteringQuery(FiltersWrapper statisticsRequest)
      throws BreakdownDeclarationFailException {
    StatisticsFilteringRequest filters = statisticsRequest.getFilters();
    StatisticsQuery query = getStatisticsQuery();

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
    return totalCount <= 0 ? 0 : Double.parseDouble(PERCENTAGE_FORMAT.format((count / totalCount) * 100.0));
  }
}
