package statistics.dashboard.common.view;

import java.util.List;

public class ListResultBreakdowns {

  private List<BreakdownResult> allBreakdowns;

  public ListResultBreakdowns(List<BreakdownResult> allBreakdowns) {
    this.allBreakdowns = allBreakdowns;
  }

  public List<BreakdownResult> getAllBreakdowns() {
    return allBreakdowns;
  }

  public void setAllBreakdowns(List<BreakdownResult> allBreakdowns) {
    this.allBreakdowns = allBreakdowns;
  }
}
