package eu.europeana.statistics.dashboard.rest;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.dto.*;
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
        Country expectedResult = prepareExpectedCountryDataResult();
        when(statisticsService.getCountryData("Netherlands")).thenReturn(expectedResult);

        Country result = controller.getCountryData("Netherlands");

        assertEquals(expectedResult, result);

    }

    @Test
    void getOverviewData_expectSuccess(){
        OverviewData expectedResult = prepareExpectedOverviewDataResult();
        when(statisticsService.getOverviewDataAllCountries()).thenReturn(expectedResult);

        OverviewData result = controller.getOverviewData();

        assertEquals(expectedResult, result);

    }

    private Country prepareExpectedCountryDataResult(){
        CurrentData elem13D = new CurrentData(2025, 1234, 25.61);
        CurrentData elem23D = new CurrentData(2030, 123456, 0.26);
        CurrentData elem1Hq = new CurrentData(2025, 4567, 12.52);
        CurrentData elem2Hq = new CurrentData(2030, 456789, 0.12);
        CurrentData elem1TotalRecords = new CurrentData(2025, 7891, 8.52);
        CurrentData elem2TotalRecords = new CurrentData(2030, 789123, 0.09);
        CurrentTarget currentTargetDataElem1 = new CurrentTarget(TargetType.THREE_D, 316, List.of(elem13D, elem23D));
        CurrentTarget currentTargetDataElem2 = new CurrentTarget(TargetType.HIGH_QUALITY, 572,
                List.of(elem1Hq, elem2Hq));
        CurrentTarget currentTargetDataElem3 = new CurrentTarget(TargetType.TOTAL_RECORDS, 672,
                List.of(elem1TotalRecords, elem2TotalRecords));
        Target target13D = new Target(TargetType.THREE_D, 306);
        Target target1Hq = new Target(TargetType.HIGH_QUALITY, 518);
        Target target1TotalRecords = new Target(TargetType.TOTAL_RECORDS, 589);
        Target target23D = new Target(TargetType.THREE_D, 316);
        Target target2Hq = new Target(TargetType.HIGH_QUALITY, 572);
        Target target2TotalRecords = new Target(TargetType.TOTAL_RECORDS, 672);
        HistoricalData historicalDataData1 = new HistoricalData(LocalDateTime.of(2024, Month.APRIL, 25, 16, 45),
                List.of(target13D, target1Hq, target1TotalRecords));
        HistoricalData historicalDataData2 = new HistoricalData(LocalDateTime.of(2024, Month.MAY, 25, 16, 45),
                List.of(target23D, target2Hq, target2TotalRecords));
        return new Country(List.of(currentTargetDataElem1, currentTargetDataElem2, currentTargetDataElem3), List.of(historicalDataData1, historicalDataData2));
    }

    private OverviewData prepareExpectedOverviewDataResult(){
        CountryOverview belgiumElem =  new CountryOverview("Belgium",
                List.of(new Target(TargetType.THREE_D, 123),
                        new Target(TargetType.HIGH_QUALITY, 456),
                        new Target(TargetType.TOTAL_RECORDS, 789)));
        CountryOverview netherlandsElem =  new CountryOverview("Netherlands",
                List.of(new Target(TargetType.THREE_D, 234),
                        new Target(TargetType.HIGH_QUALITY, 567),
                        new Target(TargetType.TOTAL_RECORDS, 891)));

        return new OverviewData(List.of(belgiumElem, netherlandsElem));
    }
}
