package targetdata;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

class HistoricalCountryTargetDataTest {

    @Test
    void testNonNullValues_expectSuccess(){
        HistoricalCountryTargetData historical = new HistoricalCountryTargetData("NL", LocalDateTime.now(), 20L, 30L, 50L);
        assertEquals(20, historical.getThreeD());
        assertEquals(30, historical.getHighQuality());
        assertEquals(50, historical.getTotalNumberRecords());
    }

    @Test
    void testNullBecomeZero_expectSuccess(){
        HistoricalCountryTargetData historical = new HistoricalCountryTargetData("NL", LocalDateTime.now(), 0L, 1L, 2L);
        assertNull(historical.getThreeD());
        assertEquals(1, historical.getHighQuality());
        assertEquals(2, historical.getTotalNumberRecords());
    }

    @Test
    void testAllNullsBecomeZeroes_expectSuccess(){
        HistoricalCountryTargetData historical = new HistoricalCountryTargetData("NL", LocalDateTime.now(), 0L, 0L, 0L);
        assertNull(historical.getThreeD());
        assertNull(historical.getHighQuality());
        assertNull(historical.getTotalNumberRecords());
    }

}
