package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;


public class HistoricalDataRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerRunner.class);

    public static void runHistoricalScript(MongoSDDao mongoSDDao){
        List<String> countries = mongoSDDao.getAllCountryValuesTargetCollection();

        for(String country : countries) {
            writeHistoricalData(country,mongoSDDao);
        }
    }


    private static void writeHistoricalData(String country, MongoSDDao mongoSDDao){
        LOGGER.info("Starting historical data process for {}", country);
        StatisticsQuery query3D = mongoSDDao.createStatisticsQuery();
        query3D.withValueFilter(MongoStatisticsField.COUNTRY, List.of(country));
        query3D.withValueFilter(MongoStatisticsField.TYPE, List.of("3D"));
        query3D.withValueFilter(MongoStatisticsField.CONTENT_TIER, List.of("1", "2", "3", "4")); //Exclude content tier 0
        StatisticsData result3dQuery = query3D.queryForStatistics();

        StatisticsQuery queryHighQuality = mongoSDDao.createStatisticsQuery();
        queryHighQuality.withValueFilter(MongoStatisticsField.COUNTRY, List.of(country));
        queryHighQuality.withValueFilter(MongoStatisticsField.CONTENT_TIER, List.of("2", "3", "4"));
        StatisticsData resultHighQualityQuery = queryHighQuality.queryForStatistics();

        StatisticsQuery queryTotalRecords = mongoSDDao.createStatisticsQuery();
        queryTotalRecords.withValueFilter(MongoStatisticsField.COUNTRY, List.of(country));
        queryTotalRecords.withValueFilter(MongoStatisticsField.CONTENT_TIER, List.of("1", "2", "3", "4")); //Exclude content tier 0
        StatisticsData resultTotalRecords = queryTotalRecords.queryForStatistics();

        Historical result = new Historical(country, result3dQuery.getRecordCount(),
                resultHighQualityQuery.getRecordCount(), resultTotalRecords.getRecordCount(), LocalDateTime.now());

        mongoSDDao.saveHistoricalRecord(result);

        LOGGER.info("Finished historical data process for {}", country);

    }


}
