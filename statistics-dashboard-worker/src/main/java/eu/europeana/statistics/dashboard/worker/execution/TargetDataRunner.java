package eu.europeana.statistics.dashboard.worker.execution;

import com.mongodb.client.MongoClient;
import eu.europeana.metis.mongo.connection.MongoClientProvider;
import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.worker.config.ConfigurationPropertiesHolder;
import eu.europeana.statistics.dashboard.worker.config.DataAccessConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//TODO: This script does not need to run frequently.
//TODO: It should only run once to fill in with the latest data about target data
//TODO: If that target data has changes, then this script needs to be updated
public class TargetDataRunner{

    private static final String COMMA_DELIMITER = ",";
    private static final Logger LOGGER = LoggerFactory.getLogger(TargetDataRunner.class);

    public static void runTargetDataScript (MongoSDDao mongoSDDao) {

        String fileName = "statistics-dashboard-worker/src/main/resources/TargetData.csv";
        List<List<String>> targetData = readCsvFile(fileName);
        writeTargetData(targetData, mongoSDDao);

    }

    private static List<List<String>> readCsvFile(String fileName) {
        List<List<String>> result = new ArrayList<>();
        LOGGER.info("Started reading document");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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

    private static void writeTargetData(List<List<String>> targetData, MongoSDDao mongoSDDao){
        final List<Target> results = new ArrayList<>();
        LOGGER.info("Started writing data into database");
        for(int i = 1; i < targetData.size(); i++){
            List<String> row = targetData.get(i);
            LOGGER.info("Started writing data of country {} into database", row.get(0));
            Target firstModel = new Target(row.get(0), Long.parseLong(row.get(3)),
                    Long.parseLong(row.get(2)), Long.parseLong(row.get(1)), 2025);
            Target secondModel = new Target(row.get(0), Long.parseLong(row.get(6)),
                    Long.parseLong(row.get(5)), Long.parseLong(row.get(4)), 2030);
            results.add(firstModel);
            results.add(secondModel);
        }

        mongoSDDao.saveTargetRecords(results);
        LOGGER.info("Finished writing data.");

    }
}
