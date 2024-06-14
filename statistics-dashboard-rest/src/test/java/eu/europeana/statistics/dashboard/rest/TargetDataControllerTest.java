package eu.europeana.statistics.dashboard.rest;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.*;
import eu.europeana.statistics.dashboard.common.internal.TargetType;
import eu.europeana.statistics.dashboard.rest.controller.TargetDataController;
import eu.europeana.statistics.dashboard.service.TargetDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TargetDataControllerTest {

    @Mock
    private TargetDataService statisticsService;

    @InjectMocks
    private TargetDataController controller;

    @Test
    void getCountryData_expectSuccess() {
        CountryDataResult expectedResult = prepareExpectedCountryDataResult();
        when(statisticsService.getCountryData("Netherlands")).thenReturn(expectedResult);

        CountryDataResult result = controller.getCountryData("Netherlands");

        assertEquals(expectedResult, result);

    }

    @Test
    void getOverviewData_expectSuccess(){
        OverviewDataResult expectedResult = prepareExpectedOverviewDataResult();
        when(statisticsService.getOverviewDataAllCountries()).thenReturn(expectedResult);

        OverviewDataResult result = controller.getOverviewData();

        assertEquals(expectedResult, result);

    }

    private CountryDataResult prepareExpectedCountryDataResult(){
        CurrentDataResult elem13D = new CurrentDataResult(2025, 1234, 25.61);
        CurrentDataResult elem23D = new CurrentDataResult(2030, 123456, 0.26);
        CurrentDataResult elem1Hq = new CurrentDataResult(2025, 4567, 12.52);
        CurrentDataResult elem2Hq = new CurrentDataResult(2030, 456789, 0.12);
        CurrentDataResult elem1TotalRecords = new CurrentDataResult(2025, 7891, 8.52);
        CurrentDataResult elem2TotalRecords = new CurrentDataResult(2030, 789123, 0.09);
        CurrentTargetDataResult currentTargetDataElem1 = new CurrentTargetDataResult(TargetType.THREE_D, 316, List.of(elem13D, elem23D));
        CurrentTargetDataResult currentTargetDataElem2 = new CurrentTargetDataResult(TargetType.HIGH_QUALITY, 572,
                List.of(elem1Hq, elem2Hq));
        CurrentTargetDataResult currentTargetDataElem3 = new CurrentTargetDataResult(TargetType.TOTAL_RECORDS, 672,
                List.of(elem1TotalRecords, elem2TotalRecords));
        TargetValue targetValue13D = new TargetValue(TargetType.THREE_D, 306);
        TargetValue targetValue1Hq = new TargetValue(TargetType.HIGH_QUALITY, 518);
        TargetValue targetValue1TotalRecords = new TargetValue(TargetType.TOTAL_RECORDS, 589);
        TargetValue targetValue23D = new TargetValue(TargetType.THREE_D, 316);
        TargetValue targetValue2Hq = new TargetValue(TargetType.HIGH_QUALITY, 572);
        TargetValue targetValue2TotalRecords = new TargetValue(TargetType.TOTAL_RECORDS, 672);
        HistoricalDataResult historicalData1 = new HistoricalDataResult(LocalDateTime.of(2024, Month.APRIL, 25, 16, 45),
                List.of(targetValue13D, targetValue1Hq, targetValue1TotalRecords));
        HistoricalDataResult historicalData2 = new HistoricalDataResult(LocalDateTime.of(2024, Month.MAY, 25, 16, 45),
                List.of(targetValue23D, targetValue2Hq, targetValue2TotalRecords));
        return new CountryDataResult(List.of(currentTargetDataElem1, currentTargetDataElem2, currentTargetDataElem3), List.of(historicalData1, historicalData2));
    }

    private OverviewDataResult prepareExpectedOverviewDataResult(){
        OverviewCountryData belgiumElem =  new OverviewCountryData("Belgium",
                List.of(new TargetValue(TargetType.THREE_D, 123),
                        new TargetValue(TargetType.HIGH_QUALITY, 456),
                        new TargetValue(TargetType.TOTAL_RECORDS, 789)));
        OverviewCountryData netherlandsElem =  new OverviewCountryData("Netherlands",
                List.of(new TargetValue(TargetType.THREE_D, 234),
                        new TargetValue(TargetType.HIGH_QUALITY, 567),
                        new TargetValue(TargetType.TOTAL_RECORDS, 891)));

        return new OverviewDataResult(List.of(belgiumElem, netherlandsElem));
    }
}
