package eu.europeana.statistics.dashboard.rest.controller;

import eu.europeana.statistics.dashboard.common.api.request.FiltersWrapper;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import eu.europeana.statistics.dashboard.service.StatisticsService;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the Statistics Dashboard {@link StatisticsService}
 */

@Tags(@Tag(name = StatisticsController.CONTROLLER_TAG_NAME, description = "Controller providing statistics values throughout Europeana database"))
@RestController
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
  @Operation(summary = "Returns a complete overview of Europeana's database")
  public ResultListFilters getGeneralStatistics(
      @Parameter(description = "Include content Tier 0")
      @RequestParam(name = "content-tier-zero", required = false) boolean contentTierZero,
      @RequestParam(value = "country", required = false, defaultValue = "") String country) {
    return contentTierZero ? statisticsService.queryGeneralEuropeanaDataIncludingContentTierZero(country) :
        statisticsService.queryGeneralEuropeanaDataWithoutContentTierZero(country);
  }

  /**
   * Get the result statistics of the given {@link StatisticsFilteringRequest}.
   *
   * @param filters - The filters and its values to apply
   * @return the statistics where the filters were applied
   */
  @PostMapping(value = FILTERING_STATISTICS, consumes = {APPLICATION_JSON}, produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Returns a complete overview of Europeana's database")
  @ApiResponse(responseCode = "400", description = "Facet declaration failed")
  public FilteringResult getFilters(
      @Parameter(description = "The filters to be applied", required = true) @RequestBody FiltersWrapper filters)
      throws BreakdownDeclarationFailException {
    return statisticsService.queryDataWithFilters(filters);
  }

  /**
   * Get all rights urls associated to given set of rightsCategories over the whole Europeana database
   *
   * @return A list of different rights urls associated to a given category
   */
  @GetMapping(value = RIGHTS_URLS, produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Returns a list of rights urls associated to a given category")
  public Set<String> getRightsUrlAssociatedToCategory(
      @Parameter(description = "Category which the urls are associated with")
      @RequestParam(name = "rightsCategories") Set<String> rightsCategoriesNames) {
    return statisticsService.getRightsUrlsWithCategory(rightsCategoriesNames.stream().map(RightsCategory::toCategoryFromName)
                                                                            .collect(Collectors.toUnmodifiableSet()));

  }
}
