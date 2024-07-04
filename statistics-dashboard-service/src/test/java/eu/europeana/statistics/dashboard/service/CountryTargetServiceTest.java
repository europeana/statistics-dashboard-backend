package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
