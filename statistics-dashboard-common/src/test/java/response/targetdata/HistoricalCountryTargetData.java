package targetdata;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

class HistoricalCountryTargetDataTest {

    @Test
    void testNullsBecomeZeroes_expectSuccess(){
        HistoricalCountryTargetData historical = new HistoricalCountryTargetData("NL", LocalDateTime.now(), 0L, 1L, 2L);
        assertNull(historical.getThreeD());
        assertEquals(historical.getHighQuality(), 1);
        assertEquals(historical.getTotalNumberRecords(), 2);
    }

}
