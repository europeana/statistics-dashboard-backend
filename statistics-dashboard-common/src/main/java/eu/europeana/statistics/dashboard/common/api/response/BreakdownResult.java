package eu.europeana.statistics.dashboard.common.api.response;

import eu.europeana.statistics.dashboard.common.internal.FacetValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Class that represents the filtering results by breakdown.
 *  It contains value to represent the breakdown and the results of each value
 */

public class BreakdownResult {

  private FacetValue breakdownBy;
  private List<StatisticsResult> results;

  public BreakdownResult(FacetValue breakdownBy, List<StatisticsResult> results) {
    this.breakdownBy = breakdownBy;
    // By ordering first and reversing after, we ensure that the highest value is the first element
    this.results = new ArrayList<>(results).stream()
        .sorted(Comparator.comparing(StatisticsResult::getCount).reversed())
        .collect(Collectors.toList());
  }

  public FacetValue getBreakdownBy() {
    return breakdownBy;
  }

  public void setBreakdownBy(String breakdownBy) {
    this.breakdownBy = FacetValue.valueOf(breakdownBy);
  }

  public List<StatisticsResult> getResults() {
    return Collections.unmodifiableList(results);
  }

  public void setResults(List<StatisticsResult> results) {
    this.results = new ArrayList<>(results).stream()
        .sorted(Comparator.comparing(StatisticsResult::getCount).reversed())
        .collect(Collectors.toList());
  }

}
