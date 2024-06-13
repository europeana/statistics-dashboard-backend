package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.*;
import eu.europeana.statistics.dashboard.common.internal.HistoricalDataModel;
import eu.europeana.statistics.dashboard.common.internal.TargetDataModel;
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
public class TargetDataServiceTest {

    @Mock
    private StatisticsData mockStatisticsData;

    @Mock
    private StatisticsQuery mockStatisticsQuery;

    @Mock
    private MongoSDDao mockMongoSDDao;

    @InjectMocks
    private TargetDataService targetDataService;

    private List<TargetDataModel> prepareMockTargetData(){
        TargetDataModel elem1 = new TargetDataModel("Netherlands", 1234, 4567, 7891, 2025);
        TargetDataModel elem2 = new TargetDataModel("Netherlands", 123456, 456789, 789123, 2030);

        return List.of(elem1, elem2);
    }

    private List<HistoricalDataModel> prepareMockHistoricalData(){
        HistoricalDataModel elem1 = new HistoricalDataModel("Netherlands", 306, 518, 589,
                LocalDateTime.of(2024, Month.APRIL, 25, 16, 45));
        HistoricalDataModel elem2 = new HistoricalDataModel("Netherlands", 316, 572, 672,
                LocalDateTime.of(2024, Month.MAY, 25, 16, 45));

        return List.of(elem1, elem2);
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

   @Test
    void getCountryData_expectSuccess(){
        when(mockMongoSDDao.getAllTargetDataOfCountry("Netherlands")).thenReturn(prepareMockTargetData());
        when(mockMongoSDDao.createStatisticsQuery()).thenReturn(mockStatisticsQuery);
        when(mockStatisticsQuery.queryForStatistics()).thenReturn(mockStatisticsData);
        when(mockStatisticsQuery.withValueFilter(any(),any())).thenReturn(mockStatisticsQuery);
        when(mockStatisticsData.getRecordCount()).thenReturn(316).thenReturn(572).thenReturn(672);
        when(mockMongoSDDao.getAllHistoricalOfCountry("Netherlands")).thenReturn(prepareMockHistoricalData());

       CountryDataResult result = targetDataService.getCountryData("Netherlands");
       CountryDataResult expected = prepareExpectedCountryDataResult();

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

        when(mockMongoSDDao.getAllCountryValuesStatisticsModel()).thenReturn(List.of("Belgium", "Netherlands"));
        when(mockMongoSDDao.createStatisticsQuery()).thenReturn(mockStatisticsQuery);
        when(mockStatisticsQuery.withValueFilter(any(),any())).thenReturn(mockStatisticsQuery);
        when(mockStatisticsQuery.queryForStatistics()).thenReturn(mockStatisticsData);
        when(mockStatisticsData.getRecordCount()).thenReturn(123).thenReturn(456).thenReturn(789)
                .thenReturn(234).thenReturn(567).thenReturn(891);


        OverviewDataResult result = targetDataService.getOverviewDataAllCountries();
        OverviewDataResult expectedResult = prepareExpectedOverviewDataResult();

        assertEquals(expectedResult.getOverviewData().size(), result.getOverviewData().size());
        for(int i = 0; i < result.getOverviewData().size(); i++){
            assertOverviewCountryDataEquals(expectedResult.getOverviewData().get(i), result.getOverviewData().get(i));
        }
    }

    private void assertCurrentTargetDataEquals(CurrentTargetDataResult expected, CurrentTargetDataResult actual) {
       assertEquals(expected.getCurrentTotalRecords(), actual.getCurrentTotalRecords());
       assertEquals(expected.getType(), actual.getType());
       assertEquals(2, actual.getCurrentData().size());
       assertCurrentDataResultEquals(expected.getCurrentData().getFirst(), actual.getCurrentData().getFirst());
       assertCurrentDataResultEquals(expected.getCurrentData().getLast(), actual.getCurrentData().getLast());
    }

    private void assertCurrentDataResultEquals(CurrentDataResult expected, CurrentDataResult actual) {
       assertEquals(expected.getTargetYear(), actual.getTargetYear());
       assertEquals(expected.getTargetValue(), actual.getTargetValue());
       assertEquals(expected.getPercentage(), actual.getPercentage());
    }

    private void assertHistoricalDataResultEquals(HistoricalDataResult expected, HistoricalDataResult actual) {
       assertEquals(expected.getTimestamp(), actual.getTimestamp());
       assertEquals(expected.getTargetValues().size(), actual.getTargetValues().size());

       for(int i=0; i<expected.getTargetValues().size(); i++) {
           assertTargetValueEquals(expected.getTargetValues().get(i), actual.getTargetValues().get(i));
       }
    }

    private void assertOverviewCountryDataEquals(OverviewCountryData expected, OverviewCountryData actual){
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getTargetData().size(), actual.getTargetData().size());

        for(int i = 0; i < expected.getTargetData().size(); i++){
            assertTargetValueEquals(expected.getTargetData().get(i), actual.getTargetData().get(i));
        }
    }

    private void assertTargetValueEquals(TargetValue expected, TargetValue actual) {
       assertEquals(expected.getTargetType(), actual.getTargetType());
       assertEquals(expected.getValue(), actual.getValue());
    }



}
