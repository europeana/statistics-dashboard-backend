package eu.europeana.statistics.dashboard.service.server;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
  public StatisticsServer(MongoSDDao mongoSDDao){
    this.mongoSDDao = mongoSDDao;
  }

  public ResultListFilters queryGeneralEuropeanaData(){
    System.out.println("Testing spring annotations and  connections");
    return null;
  }

  public FilteringResult queryDataWithFilters(StatisticsFilteringRequest statisticsFilteringRequest){
    return null;
  }

}
