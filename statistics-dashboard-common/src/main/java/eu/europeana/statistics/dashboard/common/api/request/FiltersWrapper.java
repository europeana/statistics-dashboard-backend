package eu.europeana.statistics.dashboard.common.api.request;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

/**
 * A wrapper class for the filters as the input
 */

@JsonSerialize
@ApiModel(value = "filters")
public class FiltersWrapper {

  private StatisticsFilteringRequest filters;

  /**
   * This empty constructor is needed for deserialization
   */
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
