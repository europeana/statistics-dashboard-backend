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
    HistoricalCountryTargetData data = new HistoricalCountryTargetData(
      "Germany",
        LocalDateTime.now(),
        1,
        2,
        3
      );
    List<HistoricalCountryTargetData> result = List.of(data);
    when(countryTargetService.getAllCountryData()).thenReturn(result);
    List<HistoricalCountryTargetData> testResult = controller.getAllCountryData();
    assertEquals(result, testResult);
  }

  @Test
  void getCountryTargets() {
    CountryTargetResult data = new CountryTargetResult(
      "Germany",
      TargetType.THREE_D,
      2030,
      50000);

    List<CountryTargetResult> result = List.of(data);
    when(countryTargetService.getCountryTargets()).thenReturn(result);
    List<CountryTargetResult> testResult = controller.getCountryTargets();
    assertEquals(result, testResult);
  }

}
