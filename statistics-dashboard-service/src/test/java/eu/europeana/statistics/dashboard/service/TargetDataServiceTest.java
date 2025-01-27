package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.dto.*;
import eu.europeana.statistics.dashboard.common.internal.TargetType;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TargetDataServiceTest {

    @Mock
    private StatisticsData mockStatisticsData;

    @Mock
    private StatisticsQuery mockStatisticsQuery;

    @Mock
    private MongoSDDao mockMongoSDDao;

    @InjectMocks
    private TargetDataService targetDataService;

    private List<eu.europeana.statistics.dashboard.common.internal.model.Target> prepareMockTargetData(){
        eu.europeana.statistics.dashboard.common.internal.model.Target elem1 = new eu.europeana.statistics.dashboard.common.internal.model.Target("Netherlands", 1234L, 4567L, 7891L, 2025);
        eu.europeana.statistics.dashboard.common.internal.model.Target elem2 = new eu.europeana.statistics.dashboard.common.internal.model.Target("Netherlands", 123456L, 456789L, 789123L, 2030);

        return List.of(elem1, elem2);
    }

    private List<eu.europeana.statistics.dashboard.common.internal.model.Historical> prepareMockHistoricalData(){
        eu.europeana.statistics.dashboard.common.internal.model.Historical elem1 = new eu.europeana.statistics.dashboard.common.internal.model.Historical("Netherlands", 306L, 518L, 589L,
                LocalDateTime.of(2024, Month.APRIL, 25, 16, 45));
        eu.europeana.statistics.dashboard.common.internal.model.Historical elem2 = new eu.europeana.statistics.dashboard.common.internal.model.Historical("Netherlands", 316L, 572L, 672L,
                LocalDateTime.of(2024, Month.MAY, 25, 16, 45));

        return List.of(elem1, elem2);
    }

    private Country prepareExpectedCountryDataResult(){
        CurrentData elem13D = new CurrentData(2025, 1234L, 25.61);
        CurrentData elem23D = new CurrentData(2030, 123456L, 0.26);
        CurrentData elem1Hq = new CurrentData(2025, 4567L, 12.52);
        CurrentData elem2Hq = new CurrentData(2030, 456789L, 0.12);
        CurrentData elem1TotalRecords = new CurrentData(2025, 7891L, 8.52);
        CurrentData elem2TotalRecords = new CurrentData(2030, 789123L, 0.09);
        CurrentTarget currentTargetDataElem1 = new CurrentTarget(TargetType.THREE_D, 316L, List.of(elem13D, elem23D));
        CurrentTarget currentTargetDataElem2 = new CurrentTarget(TargetType.HIGH_QUALITY, 572L,
                List.of(elem1Hq, elem2Hq));
        CurrentTarget currentTargetDataElem3 = new CurrentTarget(TargetType.TOTAL_RECORDS, 672L,
                List.of(elem1TotalRecords, elem2TotalRecords));
        Target target13D = new Target(TargetType.THREE_D, 306L);
        Target target1Hq = new Target(TargetType.HIGH_QUALITY, 518L);
        Target target1TotalRecords = new Target(TargetType.TOTAL_RECORDS, 589L);
        Target target23D = new Target(TargetType.THREE_D, 316L);
        Target target2Hq = new Target(TargetType.HIGH_QUALITY, 572L);
        Target target2TotalRecords = new Target(TargetType.TOTAL_RECORDS, 672L);
        HistoricalData historicalDataData1 = new HistoricalData(LocalDateTime.of(2024, Month.APRIL, 25, 16, 45),
                List.of(target13D, target1Hq, target1TotalRecords));
        HistoricalData historicalDataData2 = new HistoricalData(LocalDateTime.of(2024, Month.MAY, 25, 16, 45),
                List.of(target23D, target2Hq, target2TotalRecords));
        return new Country(List.of(currentTargetDataElem1, currentTargetDataElem2, currentTargetDataElem3), List.of(historicalDataData1, historicalDataData2));
    }

    private OverviewData prepareExpectedOverviewDataResult(){
        CountryOverview belgiumElem =  new CountryOverview("Belgium",
                List.of(new Target(TargetType.THREE_D, 123L),
                        new Target(TargetType.HIGH_QUALITY, 456L),
                        new Target(TargetType.TOTAL_RECORDS, 789L)));
        CountryOverview netherlandsElem =  new CountryOverview("Netherlands",
                List.of(new Target(TargetType.THREE_D, 234L),
                        new Target(TargetType.HIGH_QUALITY, 567L),
                        new Target(TargetType.TOTAL_RECORDS, 891L)));

        return new OverviewData(List.of(belgiumElem, netherlandsElem));
    }

   @Test
    void getCountryData_expectSuccess(){
        when(mockMongoSDDao.getAllTargetDataOfCountry("Netherlands")).thenReturn(prepareMockTargetData());
        when(mockMongoSDDao.createStatisticsQuery()).thenReturn(mockStatisticsQuery);
        when(mockStatisticsQuery.queryForStatistics()).thenReturn(mockStatisticsData);
        when(mockStatisticsQuery.withValueFilter(any(),any())).thenReturn(mockStatisticsQuery);
        when(mockStatisticsData.getRecordCount()).thenReturn(316L).thenReturn(572L).thenReturn(672L);
        when(mockMongoSDDao.getAllHistoricalOfCountry("Netherlands")).thenReturn(prepareMockHistoricalData());

       Country result = targetDataService.getCountryData("Netherlands");
       Country expected = prepareExpectedCountryDataResult();

        assertEquals(3, result.getCurrentTargetData().size());
       assertCurrentTargetDataEquals(expected.getCurrentTargetData().getFirst(), result.getCurrentTargetData().getFirst());
       assertCurrentTargetDataEquals(expected.getCurrentTargetData().getLast(), result.getCurrentTargetData().getLast());
       assertEquals(expected.getHistoricalData().size(), result.getHistoricalData().size());

       for(int i = 0; i < result.getHistoricalData().size(); i++){
           assertHistoricalDataResultEquals(expected.getHistoricalData().get(i), result.getHistoricalData().get(i));
       }

    }

    @Test
    void getOverviewDataAllCountries_expectSuccess() {

        when(mockMongoSDDao.getAllCountryValuesTargetCollection()).thenReturn(List.of("Belgium", "Netherlands"));
        when(mockMongoSDDao.createStatisticsQuery()).thenReturn(mockStatisticsQuery);
        when(mockStatisticsQuery.withValueFilter(any(),any())).thenReturn(mockStatisticsQuery);
        when(mockStatisticsQuery.queryForStatistics()).thenReturn(mockStatisticsData);
        when(mockStatisticsData.getRecordCount()).thenReturn(123L).thenReturn(456L).thenReturn(789L)
                .thenReturn(234L).thenReturn(567L).thenReturn(891L);


        OverviewData result = targetDataService.getOverviewDataAllCountries();
        OverviewData expectedResult = prepareExpectedOverviewDataResult();

        assertEquals(expectedResult.getOverviewData().size(), result.getOverviewData().size());
        for(int i = 0; i < result.getOverviewData().size(); i++){
            assertOverviewCountryDataEquals(expectedResult.getOverviewData().get(i), result.getOverviewData().get(i));
        }
    }

    private void assertCurrentTargetDataEquals(CurrentTarget expected, CurrentTarget actual) {
       assertEquals(expected.getCurrentTotalRecords(), actual.getCurrentTotalRecords());
       assertEquals(expected.getType(), actual.getType());
       assertEquals(2, actual.getCurrentData().size());
       assertCurrentDataResultEquals(expected.getCurrentData().getFirst(), actual.getCurrentData().getFirst());
       assertCurrentDataResultEquals(expected.getCurrentData().getLast(), actual.getCurrentData().getLast());
    }

    private void assertCurrentDataResultEquals(CurrentData expected, CurrentData actual) {
       assertEquals(expected.getTargetYear(), actual.getTargetYear());
       assertEquals(expected.getTargetValue(), actual.getTargetValue());
       assertEquals(expected.getPercentage(), actual.getPercentage());
    }

    private void assertHistoricalDataResultEquals(HistoricalData expected, HistoricalData actual) {
       assertEquals(expected.getTimestamp(), actual.getTimestamp());
       assertEquals(expected.getTargetValues().size(), actual.getTargetValues().size());

       for(int i=0; i<expected.getTargetValues().size(); i++) {
           assertTargetValueEquals(expected.getTargetValues().get(i), actual.getTargetValues().get(i));
       }
    }

    private void assertOverviewCountryDataEquals(CountryOverview expected, CountryOverview actual){
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getTargetData().size(), actual.getTargetData().size());

        for(int i = 0; i < expected.getTargetData().size(); i++){
            assertTargetValueEquals(expected.getTargetData().get(i), actual.getTargetData().get(i));
        }
    }

    private void assertTargetValueEquals(Target expected, Target actual) {
       assertEquals(expected.getTargetType(), actual.getTargetType());
       assertEquals(expected.getValue(), actual.getValue());
    }



}
