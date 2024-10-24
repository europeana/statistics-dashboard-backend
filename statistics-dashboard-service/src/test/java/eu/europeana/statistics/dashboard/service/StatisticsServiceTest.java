package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.request.FiltersWrapper;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsBreakdownValueFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsValueFilter;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private StatisticsQuery statisticsQuery;

    @Mock
    private StatisticsQuery statisticsQuery2;

    @Mock
    private StatisticsQuery statisticsQuery3;

    @Mock
    private StatisticsData statisticsData;

    @Mock
    private MongoSDDao mongoSDDao;

    private StatisticsService statisticsService;

    @NotNull
    private static FiltersWrapper getFiltersWrapper() {
        FiltersWrapper filtersWrapper = new FiltersWrapper();
        filtersWrapper.setFilters(new StatisticsFilteringRequest(
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(1),
                null,
                null,
                null,
                null,
                null,
                new StatisticsValueFilter(List.of("1")),
                new StatisticsRangeFilter("", ""),
                new StatisticsRangeFilter("", "")
        ));
        return filtersWrapper;
    }

    @NotNull
    private static FiltersWrapper getWrapper() {
        FiltersWrapper filtersWrapper = new FiltersWrapper();
        filtersWrapper.setFilters(new StatisticsFilteringRequest(
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(0),
                new StatisticsBreakdownValueFilter(0),
                new StatisticsValueFilter(List.of("1")),
                new StatisticsRangeFilter("", ""),
                new StatisticsRangeFilter("", "")
        ));
        return filtersWrapper;
    }

    @Test
    void queryGeneralEuropeanaDataWithoutContentTierZero() {
        when(statisticsData.getBreakdown()).thenReturn(List.of(statisticsData));
        when(statisticsData.getField()).thenReturn(MongoStatisticsField.CONTENT_TIER)
                .thenReturn(MongoStatisticsField.COUNTRY)
                .thenReturn(MongoStatisticsField.DATA_PROVIDER)
                .thenReturn(MongoStatisticsField.METADATA_TIER)
                .thenReturn(MongoStatisticsField.PROVIDER)
                .thenReturn(MongoStatisticsField.RIGHTS_CATEGORY)
                .thenReturn(MongoStatisticsField.TYPE);
        when(statisticsData.getRecordCount()).thenReturn(12L);
        when(statisticsData.getFieldValue())
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("3")
                .thenReturn("4");

        when(statisticsQuery3.queryForStatistics()).thenReturn(statisticsData);
        when(statisticsQuery2.withValueFilter(any(MongoStatisticsField.class), any(List.class))).thenReturn(statisticsQuery3);
        when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
        when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

        statisticsService = new StatisticsService(mongoSDDao);

        ResultListFilters resultListFilters = statisticsService.queryGeneralEuropeanaDataWithoutContentTierZero("");

        assertEquals(7, resultListFilters.getAllBreakdowns().size());
        assertTrue(List.of("contentTier", "country", "dataProvider", "metadataTier", "provider", "rightsCategory", "type").equals(
                resultListFilters.getAllBreakdowns().stream().map(b -> b.getBreakdownBy().toString()).collect(Collectors.toList())));
    }

    @Test
    void queryGeneralEuropeanaDataIncludingContentTierZero() {
        when(statisticsData.getBreakdown()).thenReturn(List.of(statisticsData));
        when(statisticsData.getField()).thenReturn(MongoStatisticsField.CONTENT_TIER)
                .thenReturn(MongoStatisticsField.COUNTRY)
                .thenReturn(MongoStatisticsField.DATA_PROVIDER)
                .thenReturn(MongoStatisticsField.METADATA_TIER)
                .thenReturn(MongoStatisticsField.PROVIDER)
                .thenReturn(MongoStatisticsField.RIGHTS_CATEGORY)
                .thenReturn(MongoStatisticsField.TYPE);
        when(statisticsData.getRecordCount()).thenReturn(12L);
        when(statisticsData.getFieldValue())
                .thenReturn("0")
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("3")
                .thenReturn("4");
        when(statisticsQuery3.queryForStatistics()).thenReturn(statisticsData);
        when(statisticsQuery2.withValueFilter(any(MongoStatisticsField.class), any(List.class))).thenReturn(statisticsQuery3);
        when(statisticsQuery2.queryForStatistics()).thenReturn(statisticsData);
        when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
        when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

        statisticsService = new StatisticsService(mongoSDDao);

        ResultListFilters resultListFilters = statisticsService.queryGeneralEuropeanaDataIncludingContentTierZero("");

        assertEquals(7, resultListFilters.getAllBreakdowns().size());
        assertTrue(List.of("contentTier", "country", "dataProvider", "metadataTier", "provider", "rightsCategory", "type").equals(
                resultListFilters.getAllBreakdowns().stream().map(b -> b.getBreakdownBy().toString()).collect(Collectors.toList())));
    }

    @Test
    void queryDataWithFilters() throws BreakdownDeclarationFailException {
        FiltersWrapper filtersWrapper = getFiltersWrapper();
        when(statisticsData.getBreakdown()).thenReturn(List.of(statisticsData));
        when(statisticsData.getField()).thenReturn(MongoStatisticsField.CONTENT_TIER)
                .thenReturn(MongoStatisticsField.CONTENT_TIER);
        when(statisticsData.getRecordCount()).thenReturn(5L);
        when(statisticsData.getFieldValue())
                .thenReturn("1")
                .thenReturn("1");
        when(statisticsQuery3.queryForStatistics()).thenReturn(statisticsData);
        when(statisticsQuery2.withValueFilter(any(MongoStatisticsField.class), any(List.class))).thenReturn(statisticsQuery3);
        when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
        when(statisticsQuery.queryForStatistics()).thenReturn(statisticsData);
        when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

        statisticsService = new StatisticsService(mongoSDDao);

        FilteringResult resultListFilters = statisticsService.queryDataWithFilters(filtersWrapper);

        assertEquals(5, resultListFilters.getResults().getCount());
        assertEquals("ALL_RECORDS", resultListFilters.getResults().getValue());
    }

    @Test
    void queryDataWithFilters_withException() {
        FiltersWrapper filtersWrapper = getWrapper();
        when(statisticsQuery3.queryForStatistics()).thenReturn(statisticsData);
        when(statisticsQuery2.withValueFilter(any(MongoStatisticsField.class), any(List.class))).thenReturn(statisticsQuery3);
        when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
        when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

        statisticsService = new StatisticsService(mongoSDDao);

        Throwable throwable = assertThrows(BreakdownDeclarationFailException.class, () ->
                statisticsService.queryDataWithFilters(filtersWrapper)
        );

        assertEquals("There are duplicate or negative breakdowns", throwable.getMessage());
    }

    @Test
    void getRightsUrlsWithCategoryTest() {
        when(statisticsData.getBreakdown()).thenReturn(List.of(statisticsData));
        when(statisticsData.getFieldValue()).thenReturn("http://exampleurl.org");
        when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
        when(statisticsQuery2.withValueFilter(any(MongoStatisticsField.class), anyList())).thenReturn(statisticsQuery3);
        when(statisticsQuery3.queryForStatistics()).thenReturn(statisticsData);
        when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);
        statisticsService = new StatisticsService(mongoSDDao);
        Set<String> result = statisticsService.getRightsUrlsWithCategory(Set.of(RightsCategory.CC0));
        assertTrue(result.contains("http://exampleurl.org"));
    }
}
