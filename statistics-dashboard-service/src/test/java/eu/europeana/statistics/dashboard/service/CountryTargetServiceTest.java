package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;
import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryTargetServiceTest {

    @Mock
    private MongoSDDao mockMongoSDDao;

    @InjectMocks
    private CountryTargetService countryTargetService;

    @Test
     void getCountryTargets_expectSuccess(){

      int[] daoResultValues = new int[]{ 100, 200, 300 };
      Target tdm = new Target(
        "Italy",
        daoResultValues[0],
        daoResultValues[1],
        daoResultValues[2],
        2025
      );
      List<Target> daoResult = List.of(tdm);

      when(mockMongoSDDao.getCountryTargets()).thenReturn(daoResult);

      List<CountryTargetResult> result = countryTargetService.getCountryTargets();

      assertEquals(tdm.getYear(), result.get(0).getTargetYear());
      assertEquals(1, daoResult.size());
      assertEquals(daoResult.size() * 3, result.size());

      assertEquals("three_d", result.get(0).getTargetType().getValue());
      assertEquals("high_quality", result.get(1).getTargetType().getValue());
      assertEquals("total", result.get(2).getTargetType().getValue());

      for (int i = 0; i < 3; i++) {
        assertEquals(daoResultValues[i], result.get(i).getValue());
      }
   }

   @Test
    void getAllCountryDataFiltered_expectSuccess(){
      final String countryCode = "NL";
       List<Historical> targetData = List.of(
               new Historical(countryCode, 123, 456, 789, LocalDateTime.MIN),
               new Historical(countryCode, 321, 654, 987, LocalDateTime.MAX)
       );
       when(mockMongoSDDao.getCountryTargetDataFiltered(any())).thenReturn(targetData);

       List<HistoricalCountryTargetData> result = countryTargetService.getAllCountryDataFiltered(countryCode);

       assertEquals(2, result.size());
       assertEquals(countryCode, result.get(0).getCountry());
       assertEquals(countryCode, result.get(1).getCountry());
       assertEquals(LocalDateTime.MIN, result.get(0).getDate());
       assertEquals(LocalDateTime.MAX, result.get(1).getDate());
       assertEquals(123, result.get(0).getThreeD());
       assertEquals(456, result.get(0).getHighQuality());
       assertEquals(789, result.get(0).getTotalNumberRecords());
       assertEquals(321, result.get(1).getThreeD());
       assertEquals(654, result.get(1).getHighQuality());
       assertEquals(987, result.get(1).getTotalNumberRecords());
   }

   @Test
    void getAllCountryDataLatest_expectSuccess(){

       List<String> countries = Arrays.asList("AT", "BE", "CZ");
       Historical historical = new Historical("will-be-overridden", 1, 2, 3, LocalDateTime.MAX);

       when(mockMongoSDDao.getAllCountryValuesTargetCollection()).thenReturn(countries);
       when(mockMongoSDDao.generateLatestTargetData(any())).thenReturn(historical);

       List<HistoricalCountryTargetData> result = countryTargetService.getAllCountryDataLatest();

       assertEquals(3, result.size());
       assertEquals(countries.get(0), result.get(0).getCountry());
       assertEquals(countries.get(1), result.get(1).getCountry());
       assertEquals(countries.get(2), result.get(2).getCountry());
       assertEquals(LocalDateTime.MAX, result.get(0).getDate());
       assertEquals(LocalDateTime.MAX, result.get(1).getDate());
       assertEquals(LocalDateTime.MAX, result.get(2).getDate());
   }
}
