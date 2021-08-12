package statistics.dashboard.common.models.count;

import java.util.List;
import statistics.dashboard.common.models.StatisticsFilter;

public abstract class StatisticsCountFilter implements StatisticsFilter{

  // It is an Integer instead of an int, so it can be null
  private Integer breakdown;
  private List<String> values;

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
