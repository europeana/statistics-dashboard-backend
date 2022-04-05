package eu.europeana.statistics.dashboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import eu.europeana.statistics.dashboard.common.api.request.FiltersWrapper;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsBreakdownValueFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsValueFilter;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsData;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @InjectMocks
  private StatisticsService statisticsService;

  @Test
  void queryGeneralEuropeanaDataWithoutContentTierZero() {
    when(statisticsData.getBreakdown()).thenReturn(List.of(statisticsData));
    when(statisticsData.getField()).thenReturn(MongoStatisticsField.CONTENT_TIER)
                                   .thenReturn(MongoStatisticsField.COUNTRY)
                                   .thenReturn(MongoStatisticsField.DATA_PROVIDER)
                                   .thenReturn(MongoStatisticsField.METADATA_TIER)
                                   .thenReturn(MongoStatisticsField.PROVIDER)
                                   .thenReturn(MongoStatisticsField.RIGHTS)
                                   .thenReturn(MongoStatisticsField.TYPE);
    when(statisticsData.getRecordCount()).thenReturn(12);
    when(statisticsData.getFieldValue())
        .thenReturn("1")
        .thenReturn("2")
        .thenReturn("3")
        .thenReturn("4");

    when(statisticsQuery3.queryForStatistics()).thenReturn(statisticsData);
    when(statisticsQuery2.withValueFilter(any(MongoStatisticsField.class), any(List.class))).thenReturn(statisticsQuery3);
    when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
    when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

    ResultListFilters resultListFilters = statisticsService.queryGeneralEuropeanaDataWithoutContentTierZero();

    assertEquals(7, resultListFilters.getAllBreakdowns().size());
    assertTrue(List.of("contentTier", "country", "dataProvider", "metadataTier", "provider", "rights", "type").equals(
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
                                   .thenReturn(MongoStatisticsField.RIGHTS)
                                   .thenReturn(MongoStatisticsField.TYPE);
    when(statisticsData.getRecordCount()).thenReturn(12);
    when(statisticsData.getFieldValue())
        .thenReturn("0")
        .thenReturn("1")
        .thenReturn("2")
        .thenReturn("3")
        .thenReturn("4");
    when(statisticsQuery2.queryForStatistics()).thenReturn(statisticsData);
    when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
    when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

    ResultListFilters resultListFilters = statisticsService.queryGeneralEuropeanaDataIncludingContentTierZero();

    assertEquals(7, resultListFilters.getAllBreakdowns().size());
    assertTrue(List.of("contentTier", "country", "dataProvider", "metadataTier", "provider", "rights", "type").equals(
        resultListFilters.getAllBreakdowns().stream().map(b -> b.getBreakdownBy().toString()).collect(Collectors.toList())));
  }

  @Test
  void queryDataWithFilters() throws BreakdownDeclarationFailException {
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
    when(statisticsData.getBreakdown()).thenReturn(List.of(statisticsData));
    when(statisticsData.getField()).thenReturn(MongoStatisticsField.CONTENT_TIER)
                                   .thenReturn(MongoStatisticsField.CONTENT_TIER);
    when(statisticsData.getRecordCount()).thenReturn(5);
    when(statisticsData.getFieldValue())
        .thenReturn("1")
        .thenReturn("1");
    when(statisticsQuery.withBreakdowns(any(MongoStatisticsField.class))).thenReturn(statisticsQuery2);
    when(statisticsQuery.queryForStatistics()).thenReturn(statisticsData);
    when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

    FilteringResult resultListFilters = statisticsService.queryDataWithFilters(filtersWrapper);

    assertEquals(5, resultListFilters.getResults().getCount());
    assertEquals("ALL_RECORDS", resultListFilters.getResults().getValue());
  }

  @Test
  void queryDataWithFilters_withException() {
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
    when(mongoSDDao.createStatisticsQuery()).thenReturn(statisticsQuery);

    Throwable throwable = assertThrows(BreakdownDeclarationFailException.class, () ->
        statisticsService.queryDataWithFilters(filtersWrapper)
    );

    assertEquals("There are duplicate or negative breakdowns", throwable.getMessage());
  }
}
