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
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryTargetDataControllerTest {

  @Mock
  private CountryTargetService countryTargetService;

  @InjectMocks
  private CountryTargetDataController controller;

  private List<HistoricalCountryTargetData> getHistoricalCountryTargetData() {
    LocalDateTime mockTime = LocalDateTime.now();
    HistoricalCountryTargetData data1 = new HistoricalCountryTargetData(
        "DE",
        mockTime,
        1L,
        2L,
        3L
      );
    return List.of(data1);
  }

  @Test
  void getCountryDataFiltered_expectSuccess() {

    List<HistoricalCountryTargetData> result = getHistoricalCountryTargetData();

    when(countryTargetService.getAllCountryDataFiltered(any())).thenReturn(
      result
    );

    List<HistoricalCountryTargetData> testResult = controller.getCountryDataFiltered("DE");
    assertEquals(result, testResult);
  }


  @Test
  void getAllCountryDataLatest_expectSuccess() {

    List<HistoricalCountryTargetData> result = getHistoricalCountryTargetData();
    when(countryTargetService.getAllCountryDataLatest()).thenReturn(result);
    List<HistoricalCountryTargetData> testResult = controller.getAllCountryDataLatest();
    assertEquals(result, testResult);

    HistoricalCountryTargetData firstResult = testResult.getFirst();
    assertEquals("DE", firstResult.getCountry());
    assertEquals(1, firstResult.getThreeD());
    assertEquals(2, firstResult.getHighQuality());
    assertEquals(3, firstResult.getTotalNumberRecords());
  }

  @Test
  void getCountryTargets_expectSuccess() {
    String countryCode = "DE";
    List<CountryTargetResult> result = List.of(
      new CountryTargetResult(
        countryCode,
        TargetType.THREE_D,
        2030,
        500L),
      new CountryTargetResult(
        countryCode,
        TargetType.HIGH_QUALITY,
        2030,
        1500L),
      new CountryTargetResult(
        countryCode,
        TargetType.TOTAL_RECORDS,
        2030,
        2500L)
    );
    when(countryTargetService.getCountryTargets()).thenReturn(result);
    List<CountryTargetResult> testResult = controller.getCountryTargets();
    assertEquals(countryCode, testResult.get(0).getCountry());
    assertEquals(countryCode, testResult.get(1).getCountry());
    assertEquals(countryCode, testResult.get(2).getCountry());
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
