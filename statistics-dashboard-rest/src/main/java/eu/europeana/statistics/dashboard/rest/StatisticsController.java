package eu.europeana.statistics.dashboard.rest;

import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import eu.europeana.statistics.dashboard.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.request.FiltersWrapper;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import java.util.Set;

/**
 * Controller for the Statistics Dashboard
 * {@link StatisticsService}
 */

@Tags(@Tag(name = StatisticsController.CONTROLLER_TAG_NAME, description = "Controller providing statistics values throughout Europeana database"))
@Api(tags = StatisticsController.CONTROLLER_TAG_NAME)
@Controller
public class StatisticsController {

  public static final String GENERAL_STATISTICS = "/statistics/europeana/general";
  public static final String FILTERING_STATISTICS = "/statistics/filtering";
  public static final String RIGHTS_URLS = "/statistics/rights/urls";
  public static final String APPLICATION_JSON = "application/json";
  public static final String CONTROLLER_TAG_NAME = "StatisticsController";

  private final StatisticsService statisticsService;

  /**
   * Autowired constructor
   */
  @Autowired
  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }


  /**
   * Get a complete statistics overview over the whole Europeana database
   *
   * @return A complete overview of Europeana database
   */
  @GetMapping(value = GENERAL_STATISTICS, produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @ApiOperation(value = "Returns a complete overview of Europeana's database", response = ResultListFilters.class)
  public ResultListFilters getGeneralStatistics(
      @ApiParam(value = "Include content Tier 0")
      @RequestParam(name="content-tier-zero", required = false) boolean contentTierZero) {
    return contentTierZero ? statisticsService.queryGeneralEuropeanaDataIncludingContentTierZero() :
            statisticsService.queryGeneralEuropeanaDataWithoutContentTierZero();
  }

  /**
   * Get the result statistics of the given {@link StatisticsFilteringRequest}.
   *
   * @param filters - The filters and its values to apply
   * @return the statistics where the filters were applied
   */
  @PostMapping(value = FILTERING_STATISTICS, consumes = {APPLICATION_JSON}, produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @ApiOperation(value = "Returns the results of the given filtering options", response = FilteringResult.class)
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Facet declaration failed")})
  public FilteringResult getFilters(
      @ApiParam(value = "The filters to be applied", required = true) @RequestBody FiltersWrapper filters)
      throws BreakdownDeclarationFailException {
    return statisticsService.queryDataWithFilters(filters);
  }

  /**
   * Get all rights urls associated to a given rightsCategory over the whole Europeana database
   *
   * @return A list of different rights urls associated to a given category
   */
  @GetMapping(value = RIGHTS_URLS, produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @ApiOperation(value = "Returns a list of rights urls associated to a given category", response = Set.class)
  public Set<String> getRightsUrlAssociatedToCategory(
          @ApiParam(value = "Category which the urls are associated with")
          @RequestParam(name= "rightsCategory") String rightsCategoryName) {
      return statisticsService.getRightsUrlsWithCategory(RightsCategory.toCategoryFromName(rightsCategoryName));

  }

}
