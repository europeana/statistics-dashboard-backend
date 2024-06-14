package eu.europeana.statistics.dashboard.rest.controller;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryDataResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.OverviewDataResult;
import eu.europeana.statistics.dashboard.service.TargetDataService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the Target Data Page
 */

@Tags(@Tag(name = TargetDataController.CONTROLLER_TAG_NAME, description = "Controller providing target data for each country"))
@Api(tags = TargetDataController.CONTROLLER_TAG_NAME)
@RestController
public class TargetDataController {

    public static final String TARGET_DATA_COUNTRY = "/statistics/europeana/target/data/{country}";
    public static final String TARGET_DATA_OVERVIEW = "/statistics/europeana/target/data/overview";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTROLLER_TAG_NAME = "TargetDataController";

    private final TargetDataService targetDataService;

    /**
     * Autowired constructor
     */
    @Autowired
    public TargetDataController(TargetDataService targetDataService) {
        this.targetDataService = targetDataService;
    }

    /**
     * Get a complete statistics overview over the whole Europeana database
     *
     * @return A complete overview of Europeana database
     * value = "{id}/progress", produces = APPLICATION_JSON_VALUE
     */
    @GetMapping(value = TARGET_DATA_COUNTRY, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns a complete overview of Europeana's database", response = CountryDataResult.class)
    public CountryDataResult getCountryData(
            @PathVariable(name = "country") String country) {
        return targetDataService.getCountryData(country);
    }

    /**
     * Get the result statistics of the given {@link StatisticsFilteringRequest}.
     *
     * @return the statistics where the filters were applied
     */
    @GetMapping(value = TARGET_DATA_OVERVIEW, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns overview current data for each type of target", response = OverviewDataResult.class)
    public OverviewDataResult getOverviewData(){
        return targetDataService.getOverviewDataAllCountries();
    }


}
