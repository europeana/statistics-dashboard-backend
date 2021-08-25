package eu.europeana.statistics.dashboard.service.server;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.BreakdownResult;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.service.persistence.Field;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for retrieving the statistics data
 * that is requested
 */
@Component
public class StatisticsServer {

  private MongoSDDao mongoSDDao;

  /**
   * Constructor for the Statistics Server
   * @param mongoSDDao - The mongo database it connects to
   */
  public StatisticsServer(MongoSDDao mongoSDDao){
    this.mongoSDDao = mongoSDDao;
  }

  public ResultListFilters queryGeneralEuropeanaData(){
    ArrayList<StatisticsData> generalQueries = prepareGeneralQueries();

    List<BreakdownResult> allBreakdowns = new ArrayList<>();

//    for(StatisticsData resultData : resultGeneralQuery.getBreakdown()){
//
//    }
    return new ResultListFilters(allBreakdowns);
  }

  public FilteringResult queryDataWithFilters(StatisticsFilteringRequest statisticsFilteringRequest){
    return null;
  }

  private ArrayList<StatisticsData> prepareGeneralQueries(){
    StatisticsQuery query = mongoSDDao.createStatisticsQuery();
    ArrayList<StatisticsData> queries = new ArrayList<>();

    List<Field> filterFields = Arrays.stream(Field.values())
        .filter(x -> x != Field.UPDATED_DATE && x != Field.CREATED_DATE && x != Field.DATASET_ID)
        .collect(Collectors.toUnmodifiableList());
    for(Field field : filterFields) {
      queries.add(query.withBreakdowns(field).queryForStatistics());
    }

    return queries;
  }

}
