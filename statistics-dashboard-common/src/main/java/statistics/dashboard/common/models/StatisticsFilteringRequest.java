package statistics.dashboard.common.models;

import java.util.List;

public class StatisticsFilteringRequest {

  private List<StatisticsFilter> filters;

  public StatisticsFilteringRequest(List<StatisticsFilter> filters) {
    this.filters = filters;
  }

  public List<StatisticsFilter> getFilters() {
    return filters;
  }

  public void setFilters(List<StatisticsFilter> filters) {
    this.filters = filters;
  }
}
