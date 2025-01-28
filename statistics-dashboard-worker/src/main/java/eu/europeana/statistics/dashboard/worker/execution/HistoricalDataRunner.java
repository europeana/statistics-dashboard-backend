package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * The runner that updates the historical data database.
 */
public final class HistoricalDataRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final MongoSDDao mongoSDDao;

  /**
   * Constructor.
   *
   * @param mongoSDDao the mongo sd dao
   */
  public HistoricalDataRunner(MongoSDDao mongoSDDao) {
    this.mongoSDDao = mongoSDDao;
  }

  @Override
  public void run(String... args) {
    LOGGER.info("Starting historical data script execution");
    List<String> countries = mongoSDDao.getAllCountryValuesStatisticsCollection();
    for (String country : countries) {
      LOGGER.info("Starting historical data process for {}", country);
      Historical result = mongoSDDao.generateLatestTargetData(country);
      LOGGER.info("Finished historical data process for {}", country);
      mongoSDDao.saveHistoricalRecord(result);
    }
    LOGGER.info("Finished historical data script execution");
  }
}
