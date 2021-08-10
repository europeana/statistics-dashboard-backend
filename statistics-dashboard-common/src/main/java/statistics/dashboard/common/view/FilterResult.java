package statistics.dashboard.common.view;

public class FilterResult {

  private String value;
  private long count;
  private double percentage;

  public FilterResult(String value, long count, double percentage) {
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


}
