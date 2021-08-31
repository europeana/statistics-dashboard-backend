package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;

/**
 * Class that represents a filter type range.
 * That is, a filtering that contains range conditions
 */

@JsonIgnoreProperties(value = { "valuesEmpty" })
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

  @Override
  public boolean isValuesEmpty() {
    return StringUtils.isEmpty(from) && StringUtils.isEmpty(to);
  }
}
