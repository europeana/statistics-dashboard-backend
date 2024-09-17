package eu.europeana.statistics.dashboard.rest.controller;
import java.util.List;

import eu.europeana.statistics.dashboard.common.internal.model.Target;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;
import eu.europeana.statistics.dashboard.service.CountryTargetService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the Target Data Page
 */

@Tags(@Tag(name = CountryTargetDataController.CONTROLLER_TAG_NAME, description = "Controller providing target data for each country"))
@Api(tags = CountryTargetDataController.CONTROLLER_TAG_NAME)
@RestController
public class CountryTargetDataController {

    public static final String TARGET_DATA_COUNTRY_ALL = "/statistics/europeana/target/country/all";
    public static final String TARGET_DATA_COUNTRY_HISTORICAL = "/statistics/europeana/target/country/historical";
    public static final String TARGET_DATA_TARGETS = "/statistics/europeana/targets";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTROLLER_TAG_NAME = "CountryTargetDataController";

    private final CountryTargetService countryTargetService;

    /**
     * Autowired constructor
     *
     * @param countryTargetService the injected service
     */
    @Autowired
    public CountryTargetDataController(CountryTargetService countryTargetService) {
        this.countryTargetService = countryTargetService;
    }

    /**
     * @return the latest country data
     */
    @GetMapping(value = TARGET_DATA_COUNTRY_HISTORICAL, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns historical country target data", response = HistoricalCountryTargetData.class)
    public List<HistoricalCountryTargetData> getCountryDataFiltered(
    @RequestParam(name = "country") String country){
      return countryTargetService.getAllCountryDataFiltered(country);
    }

    /**
     * @return the latest country data
     */
    @GetMapping(value = TARGET_DATA_COUNTRY_ALL, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns latest country target data", response = HistoricalCountryTargetData.class)
    public List<HistoricalCountryTargetData> getAllCountryDataLatest(){
      return countryTargetService.getAllCountryDataLatest();
    }

    /**
     * @return the country target result objects
     */
    @GetMapping(value = TARGET_DATA_TARGETS, produces = {APPLICATION_JSON})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns country targets", response = CountryTargetResult.class)

    public List<CountryTargetResult> getCountryTargets(){
        return countryTargetService.getCountryTargets();
    }

}
