package statistics.dashboard.common.view;

import java.util.List;

public class BreakdownResult {

  private String breakdownBy;
  private List<FilterResult> results;

  public BreakdownResult(String breakdownBy, List<FilterResult> results) {
    this.breakdownBy = breakdownBy;
    this.results = results;
  }

  public String getBreakdownBy() {
    return breakdownBy;
  }

  public void setBreakdownBy(String breakdownBy) {
    this.breakdownBy = breakdownBy;
  }

  public List<FilterResult> getResults() {
    return results;
  }

  public void setResults(List<FilterResult> results) {
    this.results = results;
  }

}
