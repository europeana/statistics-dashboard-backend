package statistics.dashboard.common.view;

import java.util.List;

public class BreakdownResult {

  private String breakdownBy;
  private List<StatisticsResult> results;

  public BreakdownResult(String breakdownBy, List<StatisticsResult> results) {
    this.breakdownBy = breakdownBy;
    this.results = results;
  }

  public String getBreakdownBy() {
    return breakdownBy;
  }

  public void setBreakdownBy(String breakdownBy) {
    this.breakdownBy = breakdownBy;
  }

  public List<StatisticsResult> getResults() {
    return results;
  }

  public void setResults(List<StatisticsResult> results) {
    this.results = results;
  }

}
