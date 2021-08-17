package statistics.dashboard.common.view;

import java.util.List;

/**
 * A wrapper class that saves all the breakdowns results
 */

public class ResultListFilters {

  private List<BreakdownResult> allBreakdowns;

  public ResultListFilters(List<BreakdownResult> allBreakdowns) {
    this.allBreakdowns = allBreakdowns;
  }

  public List<BreakdownResult> getAllBreakdowns() {
    return allBreakdowns;
  }

  public void setAllBreakdowns(List<BreakdownResult> allBreakdowns) {
    this.allBreakdowns = allBreakdowns;
  }
}
