package statistics.dashboard.common.models.count;

import java.util.List;
import statistics.dashboard.common.models.StatisticsFilter;

public abstract class StatisticsCountFilter implements StatisticsFilter{

  // It is an Integer instead of an int, so it can be null
  private Integer breakdown;
  private List<String> values;

}
