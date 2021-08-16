package statistics.dashboard.common.view;

public class StatisticsResult {

  private String value;
  private long count;
  private double percentage;
  private BreakdownResult breakdowns;

  public StatisticsResult(String value, long count, double percentage) {
    this.value = value;
    this.count = count;
    this.percentage = percentage;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public BreakdownResult getBreakdowns() {
    return breakdowns;
  }

  public void setBreakdowns(BreakdownResult breakdowns) {
    this.breakdowns = breakdowns;
  }
}
