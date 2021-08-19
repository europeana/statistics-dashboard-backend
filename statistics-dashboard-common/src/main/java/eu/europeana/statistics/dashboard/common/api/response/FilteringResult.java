package eu.europeana.statistics.dashboard.common.api.response;

/**
 * Class that saves the available filtering options and the
 * filtering results
 */

public class FilteringResult {

  private StatisticsResult results;
  private FilteringOptions filteringOptions;

  public FilteringResult(StatisticsResult results,
      FilteringOptions filteringOptions) {
    this.results = results;
    this.filteringOptions = filteringOptions;
  }

  public StatisticsResult getResults() {
    return results;
  }

  public void setResults(StatisticsResult results) {
    this.results = results;
  }

  public FilteringOptions getFilteringOptions() {
    return filteringOptions;
  }

  public void setFilteringOptions(FilteringOptions filteringOptions) {
    this.filteringOptions = filteringOptions;
  }

}
