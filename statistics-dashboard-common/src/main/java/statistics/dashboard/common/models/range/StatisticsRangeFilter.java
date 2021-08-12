package statistics.dashboard.common.models.range;

import java.util.Date;
import statistics.dashboard.common.models.StatisticsFilter;

public abstract class StatisticsRangeFilter implements StatisticsFilter{

  private Date from;
  private Date to;

  public StatisticsRangeFilter(Date from, Date to) {
    this.from = from;
    this.to = to;
  }

  public Date getFrom() {
    return from;
  }

  public void setFrom(Date from) {
    this.from = from;
  }

  public Date getTo() {
    return to;
  }

  public void setTo(Date to) {
    this.to = to;
  }
}
