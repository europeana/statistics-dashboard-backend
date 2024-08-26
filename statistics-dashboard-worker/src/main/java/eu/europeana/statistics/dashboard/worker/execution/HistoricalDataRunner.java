package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class HistoricalDataRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerRunner.class);

    public static void runHistoricalScript(MongoSDDao mongoSDDao){
        List<String> countries = mongoSDDao.getAllCountryValuesTargetCollection();
        for(String country : countries) {
            writeHistoricalData(country, mongoSDDao);
        }
    }

    private static void writeHistoricalData(String country, MongoSDDao mongoSDDao){
        Historical result = mongoSDDao.generateHistoricalSnapshot(country);
        mongoSDDao.saveHistoricalRecord(result);
    }
}
