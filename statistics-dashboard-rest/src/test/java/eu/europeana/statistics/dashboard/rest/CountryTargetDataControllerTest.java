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

    LocalDateTime mockTime = LocalDateTime.now();
    HistoricalCountryTargetData data1 = new HistoricalCountryTargetData(
      "Germany",
        mockTime,
        1,
        2,
        3
      );

    List<HistoricalCountryTargetData> result = List.of(data1);
    when(countryTargetService.getAllCountryData()).thenReturn(result);
    List<HistoricalCountryTargetData> testResult = controller.getAllCountryData();
    assertEquals(result, testResult);

    HistoricalCountryTargetData firstResult = testResult.getFirst();
    assertEquals(firstResult.getCountry(), "Germany");
    assertEquals(firstResult.getDate(), mockTime);
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
    assertEquals("Germany", testResult.get(0).getCountry());
    assertEquals("Germany", testResult.get(1).getCountry());
    assertEquals("Germany", testResult.get(2).getCountry());
    assertEquals(TargetType.THREE_D, testResult.get(0).getTargetType());
    assertEquals(TargetType.HIGH_QUALITY, testResult.get(1).getTargetType());
    assertEquals(TargetType.TOTAL_RECORDS, testResult.get(2).getTargetType());
    assertEquals(2030, testResult.get(0).getTargetYear());
    assertEquals(2030, testResult.get(1).getTargetYear());
    assertEquals(2030, testResult.get(2).getTargetYear());
    assertEquals(500, testResult.get(0).getValue());
    assertEquals(1500, testResult.get(1).getValue());
    assertEquals(2500, testResult.get(2).getValue());
  }

}
