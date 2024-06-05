package eu.europeana.statistics.dashboard.rest.controller;

import eu.europeana.statistics.dashboard.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the Statistics Dashboard {@link StatisticsService}
 */

@Tags(@Tag(name = StatisticsController.CONTROLLER_TAG_NAME, description = "Controller providing statistics values throughout Europeana database"))
@Api(tags = StatisticsController.CONTROLLER_TAG_NAME)
@RestController
public class TargetDataController {

    public static final String TARGET_DATA_COUNTRY = "/statistics/europeana/target/data/{country}";
    public static final String TARGET_DATA_OVERVIEW = "/statistics/europeana/target/data/overview";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTROLLER_TAG_NAME = "TargetDataController";

}
