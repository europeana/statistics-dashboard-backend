package eu.europeana.statistics.dashboard.service.server;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for retrieving the statistics data
 * that is requested
 */
@Service
public class StatisticsServer {

  //TODO: Mongo connection

  public StatisticsServer(){}

  public ResultListFilters queryGeneralEuropeanaData(){
    return null;
  }

  public FilteringResult queryDataWithFilters(StatisticsFilteringRequest statisticsFilteringRequest){
    return null;
  }

}
