package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class HistoricalDataRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalDataRunner.class);

    public static void runHistoricalScript(MongoSDDao mongoSDDao){
        List<String> countries = mongoSDDao.getAllCountryValuesStatisticsCollection();
        for(String country : countries) {
           LOGGER.info("Starting historical data process for {}", country);

           Historical result = mongoSDDao.generateLatestTargetData(country);

           LOGGER.info("Finished historical data process for {}", country);

           mongoSDDao.saveHistoricalRecord(result);
        }
    }
}
