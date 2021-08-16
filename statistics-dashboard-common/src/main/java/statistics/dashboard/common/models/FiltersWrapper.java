package statistics.dashboard.common.models;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

@JsonSerialize
@ApiModel
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
