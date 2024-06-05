package eu.europeana.statistics.dashboard.common.api.response.targetdata;

/**
 * Class that saves the target values
 */
public class CurrentDataResult {

    private Integer targetYear;
    private Integer targetValue;
    private Integer percentage;

    public CurrentDataResult(Integer targetYear, Integer targetValue, Integer percentage) {
        this.targetYear = targetYear;
        this.targetValue = targetValue;
        this.percentage = percentage;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
