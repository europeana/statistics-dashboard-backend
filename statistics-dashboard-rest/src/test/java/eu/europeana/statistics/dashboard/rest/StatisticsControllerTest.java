package eu.europeana.statistics.dashboard.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import eu.europeana.statistics.dashboard.common.api.request.FiltersWrapper;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsBreakdownValueFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsValueFilter;
import eu.europeana.statistics.dashboard.common.api.response.FilteringOptions;
import eu.europeana.statistics.dashboard.common.api.response.FilteringResult;
import eu.europeana.statistics.dashboard.common.api.response.ResultListFilters;
import eu.europeana.statistics.dashboard.common.api.response.StatisticsResult;
import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import eu.europeana.statistics.dashboard.rest.controller.StatisticsController;
import eu.europeana.statistics.dashboard.service.StatisticsService;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

  @Mock
  private StatisticsService statisticsService;

  @InjectMocks
  private StatisticsController controller;

  @Test
  void getGeneralStatisticsWithoutContentTierZero() {
    ResultListFilters resultListFilters = new ResultListFilters(List.of());
    when(statisticsService.queryGeneralEuropeanaDataWithoutContentTierZero("")).thenReturn(resultListFilters);

    ResultListFilters testResult = controller.getGeneralStatistics(false, "");

    assertEquals(resultListFilters, testResult);
  }

  @Test
  void getGeneralStatisticsIncludingContentTierZero() {
    ResultListFilters resultListFilters = new ResultListFilters(List.of());
    when(statisticsService.queryGeneralEuropeanaDataIncludingContentTierZero("")).thenReturn(resultListFilters);

    ResultListFilters testResult = controller.getGeneralStatistics(true, "");

    assertEquals(resultListFilters, testResult);
  }

  @Test
  void getFilters() throws BreakdownDeclarationFailException {
    FiltersWrapper filtersWrapper = new FiltersWrapper();
    FilteringResult filteringResult = new FilteringResult(new StatisticsResult("value", 5L, 100L),
        new FilteringOptions(new HashSet<>() {{
          add("a");
        }},
            new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
            new HashSet<>(), new HashSet<>(), null, null));
    when(statisticsService.queryDataWithFilters(any())).thenReturn(filteringResult);

    FilteringResult testFilteringResult = controller.getFilters(filtersWrapper);

    assertEquals(testFilteringResult, filteringResult);
  }

  @Test
  void getRightsUrlAssociatedToCategoryTest_expectSuccess() {
    Set<String> mockAnswer = Set.of("url1", "url2", "url3");

    when(statisticsService.getRightsUrlsWithCategory(anySet())).thenReturn(mockAnswer);
    Set<String> result = controller.getRightsUrlAssociatedToCategory(Set.of(RightsCategory.CC_BY.getName()));

    assertEquals(mockAnswer, result);
  }

  @Test
  void getFiltersWithException() {
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

    Throwable testResult = assertThrows(BreakdownDeclarationFailException.class, () ->
    {
      when(statisticsService.queryDataWithFilters(any())).thenThrow(new BreakdownDeclarationFailException("Error"));
      controller.getFilters(filtersWrapper);
    });
    assertEquals("Error", testResult.getMessage());
  }
}
