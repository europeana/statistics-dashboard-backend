package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;
import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    void getAllCountryData_expectSuccess(){
       List<Historical> targetData = List.of(
               new Historical("Netherlands", 123, 456, 789, LocalDateTime.MIN),
               new Historical("Netherlands", 321, 654, 987, LocalDateTime.MAX),
               new Historical("Germany", 147, 528, 963, LocalDateTime.MIN),
               new Historical("Germany", 741, 852, 1256, LocalDateTime.MAX)
       );

       when(mockMongoSDDao.getCountryTargetData()).thenReturn(targetData);

       List<HistoricalCountryTargetData> result = countryTargetService.getAllCountryData();

       assertEquals(4, result.size());
       assertEquals("Netherlands", result.get(0).getCountry());
       assertEquals("Netherlands", result.get(1).getCountry());
       assertEquals("Germany", result.get(2).getCountry());
       assertEquals("Germany", result.get(3).getCountry());
       assertEquals(LocalDateTime.MIN, result.get(0).getDate());
       assertEquals(LocalDateTime.MAX, result.get(1).getDate());
       assertEquals(LocalDateTime.MIN, result.get(2).getDate());
       assertEquals(LocalDateTime.MAX, result.get(3).getDate());
       assertEquals(123, result.get(0).getThreeD());
       assertEquals(456, result.get(0).getHighQuality());
       assertEquals(789, result.get(0).getTotalNumberRecords());
       assertEquals(321, result.get(1).getThreeD());
       assertEquals(654, result.get(1).getHighQuality());
       assertEquals(987, result.get(1).getTotalNumberRecords());
       assertEquals(147, result.get(2).getThreeD());
       assertEquals(528, result.get(2).getHighQuality());
       assertEquals(963, result.get(2).getTotalNumberRecords());
       assertEquals(741, result.get(3).getThreeD());
       assertEquals(852, result.get(3).getHighQuality());
       assertEquals(1256, result.get(3).getTotalNumberRecords());
   }

}
