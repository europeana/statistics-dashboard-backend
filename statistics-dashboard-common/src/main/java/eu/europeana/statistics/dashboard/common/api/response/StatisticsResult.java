package eu.europeana.statistics.dashboard.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Class that represents a single filtering result with its
 * value, the count of records, the percentage of that count, and
 * breakdowns if there are any
 */

@JsonInclude(Include.NON_NULL)
public class StatisticsResult {

  private String value;
  private Long count;
  private double percentage;
  private BreakdownResult breakdowns;

  public StatisticsResult(String value, Long count, double percentage) {
    this.value = value;
    this.count = count;
    this.percentage = percentage;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public BreakdownResult getBreakdowns() {
    return breakdowns;
  }

  public void setBreakdowns(BreakdownResult breakdowns) {
    this.breakdowns = breakdowns;
  }
}
