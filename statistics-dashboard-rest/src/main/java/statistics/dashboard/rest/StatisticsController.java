package statistics.dashboard.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import statistics.dashboard.common.models.StatisticsFilteringRequest;
import statistics.dashboard.common.view.FilteringResult;
import statistics.dashboard.common.view.FiltersWrapper;
import statistics.dashboard.common.view.ResultListFilters;

/**
 * Controller for the Statistics Dashboard
 */

@Api("/")
@Controller
public class StatisticsController {

  public static final String GENERAL_STATISTICS = "/statistics/europeana/general";
  public static final String FILTERING_STATISTICS = "statistics/filtering";
  public static final String APPLICATION_JSON = "application/json";

  /**
   * Get a complete statistics overview over the whole Europeana database
   *
   * @return A complete overview of Europeana database
   */
  @GetMapping(value = GENERAL_STATISTICS, produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @ApiOperation(value = "Returns a complete overview of Europeana's database", response = ResultListFilters.class)
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Error processing the result")})
  public ResultListFilters getGeneralStatistics() {
    return null;
  }

  /**
   * Get the result statistics of the given {@link StatisticsFilteringRequest}.
   *
   * @param filters - The filters and its values to apply
   * @return the statistics where the filters were applied
   */
  @PostMapping(value = FILTERING_STATISTICS, consumes = {APPLICATION_JSON},
      produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @ApiOperation(value = "Returns the results of the given filtering options", response = FilteringResult.class)
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Error processing the result")})
  public FilteringResult getFilters(@ApiParam(value = "The filters to be applied", required = true)
      @RequestBody FiltersWrapper filters) {
    return null;
  }

}
