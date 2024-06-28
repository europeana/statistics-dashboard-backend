package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDTargetsDao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryTargetServiceTest {

    @Mock
    private MongoSDTargetsDao mockMongoSDTargetsDao;

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

      when(mockMongoSDTargetsDao.getCountryTargets()).thenReturn(daoResult);

      List<CountryTargetResult> result = countryTargetService.getCountryTargets();

      assertEquals(Integer.toString(tdm.getYear()), result.get(0).getLabel());
      assertEquals(true, result.get(0).getInterim());
      assertEquals(1, daoResult.size());
      assertEquals(daoResult.size() * 3, result.size());

      int i = 0;
      for (String s : countryTargetService.getTargetTypes()) {
        assertEquals(s, result.get(i).getTargetType());
        i++;
      }
      for (i = 0; i < 3; i++) {
        assertEquals(daoResultValues[i], result.get(i).getValue());
      }

     }

}
