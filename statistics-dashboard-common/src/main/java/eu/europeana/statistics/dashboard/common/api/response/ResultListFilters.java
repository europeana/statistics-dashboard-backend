package eu.europeana.statistics.dashboard.common.api.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper class that saves all the breakdowns results
 */

public class ResultListFilters {

  private List<BreakdownResult> allBreakdowns;

  public ResultListFilters(List<BreakdownResult> allBreakdowns) {
    this.allBreakdowns = new ArrayList<>(allBreakdowns);
  }

  public List<BreakdownResult> getAllBreakdowns() {
    return Collections.unmodifiableList(allBreakdowns);
  }

  public void setAllBreakdowns(List<BreakdownResult> allBreakdowns) {
    this.allBreakdowns = new ArrayList<>(allBreakdowns);
  }
}
