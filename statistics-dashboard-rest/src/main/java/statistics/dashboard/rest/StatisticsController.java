package statistics.dashboard.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import statistics.dashboard.common.models.StatisticsFilteringRequest;
import statistics.dashboard.common.view.FilteringResult;
import statistics.dashboard.common.view.ResultListFilters;

@Controller
public class StatisticsController {

  public static final String GENERAL_STATISTICS = "/statistics/europeana/general";
  public static final String FILTERING_STATISTICS = "statistics/filtering";
  public static final String APPLICATION_JSON = "application/json";

  /**
   *
   * @return
   */
  @GetMapping(value = GENERAL_STATISTICS, consumes = {APPLICATION_JSON},
      produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResultListFilters getGeneralStatistics(){
    return null;
  }

  /**
   *
   * @param filters
   * @return
   */
  @PostMapping(value = FILTERING_STATISTICS, consumes = {APPLICATION_JSON},
      produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  public FilteringResult getFilters(@RequestBody StatisticsFilteringRequest filters){
    return null;
  }

}
