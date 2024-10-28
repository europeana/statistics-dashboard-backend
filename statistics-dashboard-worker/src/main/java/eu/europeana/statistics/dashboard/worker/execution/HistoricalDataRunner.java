package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Historical data runner.
 */
public final class HistoricalDataRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalDataRunner.class);

  private HistoricalDataRunner() {
    // no op
  }

  /**
   * Run historical script.
   *
   * @param mongoSDDao the mongo sd dao
   */
  public static void runHistoricalScript(MongoSDDao mongoSDDao) {
    List<String> countries = mongoSDDao.getAllCountryValuesStatisticsCollection();
    for (String country : countries) {
      LOGGER.info("Starting historical data process for {}", country);

      Historical result = mongoSDDao.generateLatestTargetData(country);

      LOGGER.info("Finished historical data process for {}", country);

      mongoSDDao.saveHistoricalRecord(result);
    }
  }
}
