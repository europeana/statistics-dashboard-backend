package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class that represents a filter type count. That is, a filtering that results in a count of records with the conditions met
 */

@JsonSerialize
public class StatisticsBreakdownValueFilter extends StatisticsValueFilter {

  // It is an Integer instead of an int, so it can be null
  private Integer breakdown;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsBreakdownValueFilter() {
  }

  public StatisticsBreakdownValueFilter(Integer breakdown) {
    this.breakdown = breakdown;
  }

  public Integer getBreakdown() {
    return breakdown;
  }

  public void setBreakdown(Integer breakdown) {
    this.breakdown = breakdown;
  }
}
