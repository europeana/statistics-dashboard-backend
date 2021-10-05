package eu.europeana.statistics.dashboard.common.api.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;

/**
 * A wrapper class for the filters as the input
 */

@JsonSerialize
public class FiltersWrapper {

  @JsonProperty(FilterNames.DATASET_ID)
  private String datasetId;
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

  public String getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }
}
