package eu.europeana.statistics.dashboard.rest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.internal.TargetType;
import eu.europeana.statistics.dashboard.service.CountryTargetService;
import eu.europeana.statistics.dashboard.rest.controller.CountryTargetDataController;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryTargetDataControllerTest {

  @Mock
  private CountryTargetService countryTargetService;

  @InjectMocks
  private CountryTargetDataController controller;

  @Test
  void getAllCountryData() {

    HistoricalCountryTargetData data1 = new HistoricalCountryTargetData(
      "Germany",
        LocalDateTime.now(),
        1,
        2,
        3
      );
    
    List<HistoricalCountryTargetData> result = List.of(data1);
    when(countryTargetService.getAllCountryData()).thenReturn(result);
    List<HistoricalCountryTargetData> testResult = controller.getAllCountryData();
    assertEquals(result, testResult);

    HistoricalCountryTargetData firstResult = testResult.get(0);
    assertEquals(firstResult.getCountry(), "Germany");
    assertEquals(firstResult.getThreeD(), 1);
    assertEquals(firstResult.getHighQuality(), 2);
    assertEquals(firstResult.getTotalNumberRecords(), 3);
  }

  @Test
  void getCountryTargets() {
    List<CountryTargetResult> result = List.of(
      new CountryTargetResult(
        "Germany",
        TargetType.THREE_D,
        2030,
        500),
      new CountryTargetResult(
        "Germany",
        TargetType.HIGH_QUALITY,
        2030,
        1500),
      new CountryTargetResult(
        "Germany",
        TargetType.TOTAL_RECORDS,
        2030,
        2500)
    );
    when(countryTargetService.getCountryTargets()).thenReturn(result);
    List<CountryTargetResult> testResult = controller.getCountryTargets();
    assertEquals(result, testResult);
    assertEquals(testResult.get(0).getTargetType(), TargetType.THREE_D);
    assertEquals(testResult.get(1).getTargetType(), TargetType.HIGH_QUALITY);
    assertEquals(testResult.get(2).getTargetType(), TargetType.TOTAL_RECORDS);
  }

}
