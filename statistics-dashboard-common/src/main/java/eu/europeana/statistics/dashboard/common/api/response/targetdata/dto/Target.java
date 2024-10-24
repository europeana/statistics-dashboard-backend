package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import eu.europeana.statistics.dashboard.common.internal.TargetType;

/**
 * Class that has an overview of the current value for a target type
 */
public class Target {

    private TargetType targetType;
    private Long value;

    public Target(TargetType targetType, Long value) {
        this.targetType = targetType;
        this.value = value;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
