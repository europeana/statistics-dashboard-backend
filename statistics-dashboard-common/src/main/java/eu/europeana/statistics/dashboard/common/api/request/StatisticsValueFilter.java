package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * Class that represents a filter type count.
 * That is, a filtering that results in a count of records with the conditions met
 */

@JsonIgnoreProperties(value = { "valuesEmpty" })
@JsonSerialize
public class StatisticsValueFilter implements StatisticsFilter{

  // It is an Integer instead of an int, so it can be null
  private Integer breakdown;
  private List<String> values;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsValueFilter(){}

  public StatisticsValueFilter(Integer breakdown, List<String> values) {
    this.breakdown = breakdown;
    this.values = new ArrayList<>(values);
  }

  public Integer getBreakdown() {
    return breakdown;
  }

  public void setBreakdown(Integer breakdown) {
    this.breakdown = breakdown;
  }

  public List<String> getValues() {
    return Collections.unmodifiableList(values);
  }

  public void setValues(List<String> values) {
    this.values = new ArrayList<>(values);
  }

  @Override
  public boolean isValuesEmpty() {
    return CollectionUtils.isEmpty(values);
  }
}
