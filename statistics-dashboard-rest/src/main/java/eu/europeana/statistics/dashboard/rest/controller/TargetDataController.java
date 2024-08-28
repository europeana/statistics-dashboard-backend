package eu.europeana.statistics.dashboard.rest.controller;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.dto.Country;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.dto.OverviewData;
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
     * Returns all the historical data based on the given country, including their latest values per target data
     *
     * @return Historical data based on the given country
     */
    @GetMapping(value = TARGET_DATA_COUNTRY, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns historical data based on the given country", response = Country.class)
    public Country getCountryData(
            @PathVariable(name = "country") String country) {
        return targetDataService.getCountryData(country);
    }

    /**
     * Returns the latest values of each target data for all countries
     *
     * @return A list of the latest values of each target data for all countries
     */
    @GetMapping(value = TARGET_DATA_OVERVIEW, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns the latest target data for all countries", response = OverviewData.class)
    public OverviewData getOverviewData(){
        return targetDataService.getOverviewDataAllCountries();
    }


}
