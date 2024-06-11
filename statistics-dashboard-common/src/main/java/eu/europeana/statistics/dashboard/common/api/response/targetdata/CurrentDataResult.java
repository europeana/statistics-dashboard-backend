package eu.europeana.statistics.dashboard.common.api.response.targetdata;

/**
 * Class that saves the target values
 */
public class CurrentDataResult {

    private int targetYear;
    private int targetValue;
    private double percentage;

    public CurrentDataResult(Integer targetYear, Integer targetValue, double percentage) {
        this.targetYear = targetYear;
        this.targetValue = targetValue;
        this.percentage = percentage;
    }

    public int getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(int targetYear) {
        this.targetYear = targetYear;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
