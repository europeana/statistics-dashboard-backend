package statistics.dashboard.common.models.filters;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class that represents a filter type range.
 * That is, a filtering that contains range conditions
 */

@JsonSerialize
public class StatisticsRangeFilter implements StatisticsFilter{

  private String from;
  private String to;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsRangeFilter(){}

  public StatisticsRangeFilter(String from, String to) {
    this.from = from;
    this.to = to;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
