package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interface to represent filters for Statistics Dashboard
 */
public interface StatisticsFilter {

  @JsonIgnore
  boolean isValuesEmpty();

}