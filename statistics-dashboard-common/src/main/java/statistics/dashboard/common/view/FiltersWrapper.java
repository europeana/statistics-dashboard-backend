package statistics.dashboard.common.view;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import statistics.dashboard.common.models.StatisticsFilteringRequest;

/**
 * A wrapper class for the filters as the input
 */

@JsonSerialize
@ApiModel(value = "filters")
public class FiltersWrapper {

  private StatisticsFilteringRequest filters;

  public FiltersWrapper(){
  }

  public FiltersWrapper(StatisticsFilteringRequest filters) {
    this.filters = filters;
  }

  public StatisticsFilteringRequest getFilters() {
    return filters;
  }

  public void setFilters(StatisticsFilteringRequest filters) {
    this.filters = filters;
  }
}
