package eu.europeana.statistics.dashboard.service.persistence;

import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsDataTest {

    private StatisticsData statisticsData;

    @Test
    void getField() {
        statisticsData = new StatisticsData(MongoStatisticsField.RIGHTS, "CC_BY", List.of());
        assertEquals(MongoStatisticsField.RIGHTS, statisticsData.getField());
    }

    @Test
    void getFieldValue() {
        statisticsData = new StatisticsData(MongoStatisticsField.COUNTRY, "NETHERLANDS", List.of());
        assertEquals("NETHERLANDS", statisticsData.getFieldValue());
    }

    @Test
    void getRecordCount() {
        statisticsData = new StatisticsData(MongoStatisticsField.CONTENT_TIER, "4", List.of());
        assertEquals(0, statisticsData.getRecordCount());
    }

    @Test
    void getBreakdown() {
        StatisticsData item = new StatisticsData(MongoStatisticsField.COUNTRY, "NETHERLANDS", 5);
        statisticsData = new StatisticsData(MongoStatisticsField.METADATA_TIER, "A",
                List.of(item));
        assertIterableEquals(List.of(item), statisticsData.getBreakdown());
    }

    @Test
    void getBreakdownWhenNullAsEmptyList() {
        statisticsData = new StatisticsData(MongoStatisticsField.METADATA_TIER, "A", null);
        assertIterableEquals(List.of(), statisticsData.getBreakdown());
    }

    @Test
    void isBreakdownListEmpty() {
        statisticsData = new StatisticsData(MongoStatisticsField.DATA_PROVIDER, "Kunst", List.of());
        assertTrue(statisticsData.isBreakdownListEmpty());
    }
}
