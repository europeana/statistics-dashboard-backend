package eu.europeana.statistics.dashboard.common.api.response;

import eu.europeana.statistics.dashboard.common.iternal.FacetValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Class that represents the filtering results by breakdown.
 *  It contains value to represent the breakdown and the results of each value
 */

public class BreakdownResult {

  private String breakdownBy;
  private List<StatisticsResult> results;

  public BreakdownResult(String breakdownBy, List<StatisticsResult> results) {
    this.breakdownBy = breakdownBy;
    this.results = new ArrayList<>(results);
  }

  public String getBreakdownBy() {
    return breakdownBy;
  }

  public void setBreakdownBy(String breakdownBy) {
    this.breakdownBy = breakdownBy;
  }

  public List<StatisticsResult> getResults() {
    return Collections.unmodifiableList(results);
  }

  public void setResults(List<StatisticsResult> results) {
    this.results = new ArrayList<>(results);
  }

}
