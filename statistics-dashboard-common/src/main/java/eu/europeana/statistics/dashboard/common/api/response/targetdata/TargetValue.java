package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import eu.europeana.statistics.dashboard.common.internal.TargetType;

/**
 * Class that has an overview of the current value for a target type
 */
public class TargetValue {

    private TargetType targetType;
    private int value;

    public TargetValue(TargetType targetType, int value) {
        this.targetType = targetType;
        this.value = value;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
