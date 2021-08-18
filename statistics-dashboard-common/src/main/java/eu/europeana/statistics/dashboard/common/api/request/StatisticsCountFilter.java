package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

/**
 * Class that represents a filter type count.
 * That is, a filtering that results in a count of records with the conditions met
 */

@JsonSerialize
public class StatisticsCountFilter implements StatisticsFilter{

  // It is an Integer instead of an int, so it can be null
  private Integer breakdown;
  private List<String> values;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsCountFilter(){}

  public StatisticsCountFilter(Integer breakdown, List<String> values) {
    this.breakdown = breakdown;
    this.values = values;
  }

  public Integer getBreakdown() {
    return breakdown;
  }

  public void setBreakdown(Integer breakdown) {
    this.breakdown = breakdown;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }
}
