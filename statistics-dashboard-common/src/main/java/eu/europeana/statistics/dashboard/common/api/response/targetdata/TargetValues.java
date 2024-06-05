package eu.europeana.statistics.dashboard.common.api.response.targetdata;

/**
 * Class that has an overview of the current value for a target type
 */
public class TargetValues {

    private String targetType;
    private String value;

    public TargetValues(String targetType, String value) {
        this.targetType = targetType;
        this.value = value;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
