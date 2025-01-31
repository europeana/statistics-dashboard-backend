package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * The runner that creates the Target data database.
 * <p>This runner is only ran if the Target data database does not already exist.</p>
 */
public final class TargetDataRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String COMMA_DELIMITER = ",";
  private static final String TARGET_DATA_CSV = "TargetData.csv";
  private final MongoSDDao mongoSDDao;
  private final ResourceLoader resourceLoader;

  /**
   * Constructor.
   *
   * @param mongoSDDao the mongo sd dao
   * @param resourceLoader the resource loader
   */
  public TargetDataRunner(MongoSDDao mongoSDDao, ResourceLoader resourceLoader) {
    this.mongoSDDao = mongoSDDao;
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void run(String... args) {
    if (!mongoSDDao.doesCollectionDataExist("Target")) {
      LOGGER.info("Starting target script execution");
      Resource resource = resourceLoader.getResource("classpath:" + TARGET_DATA_CSV);
      List<List<String>> targetData = readCsvFile(resource);
      writeTargetData(targetData, mongoSDDao);
      LOGGER.info("Finished target script execution");
    }
  }

  private List<List<String>> readCsvFile(Resource resource) {
    List<List<String>> result = new ArrayList<>();
    LOGGER.info("Started reading document");
    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(COMMA_DELIMITER);
        result.add(Arrays.asList(values));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    LOGGER.info("Finished reading document");
    return result;
  }

  private void writeTargetData(List<List<String>> targetData, MongoSDDao mongoSDDao) {
    final List<Target> results = new ArrayList<>();
    LOGGER.info("Started writing data into database");
    for (int i = 1; i < targetData.size(); i++) {
      List<String> row = targetData.get(i);
      LOGGER.info("Started writing data of country {} into database", row.get(0));
      Target firstModel = new Target(row.get(0), Integer.parseInt(row.get(3)),
          Integer.parseInt(row.get(2)), Integer.parseInt(row.get(1)), 2025);
      Target secondModel = new Target(row.get(0), Integer.parseInt(row.get(6)),
          Integer.parseInt(row.get(5)), Integer.parseInt(row.get(4)), 2030);
      results.add(firstModel);
      results.add(secondModel);
    }

    mongoSDDao.saveTargetRecords(results);
    LOGGER.info("Finished writing data.");

  }
}
