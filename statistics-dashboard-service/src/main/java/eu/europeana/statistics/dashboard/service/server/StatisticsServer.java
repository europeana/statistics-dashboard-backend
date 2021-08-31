package eu.europeana.statistics.dashboard.service.server;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.BreakdownResult;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.api.response.StatisticsResult;
import eu.europeana.statistics.dashboard.common.iternal.FacetValue;
import eu.europeana.statistics.dashboard.service.utils.RequestUtils;
import eu.europeana.statistics.dashboard.service.exception.FailedFieldException;
import eu.europeana.statistics.dashboard.common.iternal.FieldMongoStatistics;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for retrieving the statistics data that is requested
 */
@Component
public class StatisticsServer {

  private MongoSDDao mongoSDDao;

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
   * @throws FailedFieldException
   */
  public ResultListFilters queryGeneralEuropeanaData() throws FailedFieldException {
    List<StatisticsData> generalQueries = prepareGeneralQueries();
    int totalRecordCount = generalQueries.get(0).getRecordCount();

    List<BreakdownResult> allBreakdowns = new ArrayList<>();
    for (StatisticsData resultData : generalQueries) {

      // Replace Field with FacetValue object
      FacetValue breakdownBy = FacetValue.fromFieldToFacetValue(
          resultData.getBreakdown().get(0).getField());

      // Check if any FacetValue was found from Field
      if(breakdownBy == null){
        throw new FailedFieldException("No such field " + resultData.getBreakdown().get(0).getField()
            + " exists");
      }

      // Convert StatisticsData into a list of StatisticResult
      List<StatisticsResult> statisticsResultList = resultData.getBreakdown().stream()
          .map(data -> new StatisticsResult(data.getFieldValue(), data.getRecordCount(),
              getPercentage(totalRecordCount, data.getRecordCount())))
          .collect(Collectors.toList());

      allBreakdowns.add(new BreakdownResult(breakdownBy, statisticsResultList));

    }
    return new ResultListFilters(allBreakdowns);
  }

  /**
   *
   * @param statisticsRequest
   * @return
   */
  public FilteringResult queryDataWithFilters(StatisticsFilteringRequest statisticsRequest) {
    StatisticsData queryResult = prepareFilteringQuery(statisticsRequest);
    return null;
  }

  private List<StatisticsData> prepareGeneralQueries() {
    StatisticsQuery query = mongoSDDao.createStatisticsQuery();
    List<StatisticsData> queries = new ArrayList<>();

    List<FieldMongoStatistics> filterFieldMongoStatistics = Arrays.stream(FieldMongoStatistics.values())
        .filter(field -> field != FieldMongoStatistics.UPDATED_DATE &&
            field != FieldMongoStatistics.CREATED_DATE &&
            field != FieldMongoStatistics.DATASET_ID)
        .collect(Collectors.toUnmodifiableList());
    for (FieldMongoStatistics fieldMongoStatistics : filterFieldMongoStatistics) {
      queries.add(query.withBreakdowns(fieldMongoStatistics).queryForStatistics());
    }

    return queries;
  }

  private StatisticsData prepareFilteringQuery(StatisticsFilteringRequest statisticsRequest){
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

    return query.queryForStatistics();

  }

  private double getPercentage(double totalCount, double count) {
    return (count / totalCount) * 100;
  }

}
