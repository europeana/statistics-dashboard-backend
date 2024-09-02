package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import eu.europeana.statistics.dashboard.common.internal.TargetType;

/**
 * Class that has an overview of the current value for a target type
 */
public class Target {

    private TargetType targetType;
    private int value;

    public Target(TargetType targetType, int value) {
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
