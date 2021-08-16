package statistics.dashboard.common.models.filters;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import java.util.List;

@JsonSerialize
@ApiModel(value = "statisticsCountFilter")
public class StatisticsCountFilter implements StatisticsFilter{

  // It is an Integer instead of an int, so it can be null
  private Integer breakdown;
  private List<String> values;

  public StatisticsCountFilter(){

  }

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
