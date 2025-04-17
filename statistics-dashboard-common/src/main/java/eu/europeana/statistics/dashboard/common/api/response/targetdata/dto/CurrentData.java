package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

/**
 * Class that saves the target values
 */
public class CurrentData {

    private int targetYear;
    private Long targetValue;
    private double percentage;

    public CurrentData(Integer targetYear, Long targetValue, double percentage) {
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

    public Long getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Long targetValue) {
        this.targetValue = targetValue;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
