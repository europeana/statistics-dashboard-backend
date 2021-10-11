package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * Similarly to filter type {@link StatisticsBreakdownValueFilter} but this class does not contain breakdowns
 */

@JsonSerialize
public class StatisticsValueFilter implements StatisticsFilter{

  private List<String> values;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsValueFilter() {
  }

  public StatisticsValueFilter(List<String> values) {
    this.values = new ArrayList<>(values);
  }

  public List<String> getValues() {
    return values == null ? new ArrayList<>() : Collections.unmodifiableList(values);
  }

  public void setValues(List<String> values) {
    this.values = new ArrayList<>(values);
  }

  @Override
  public boolean isValuesEmpty() {
    return CollectionUtils.isEmpty(values);
  }
}
