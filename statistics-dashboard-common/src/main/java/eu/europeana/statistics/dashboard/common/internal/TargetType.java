package eu.europeana.statistics.dashboard.common.internal;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TargetType {

    THREE_D("three_d"),
    HIGH_QUALITY("high_quality"),
    TOTAL_RECORDS("total"),;

    private final String valueAsString;

    TargetType(String value){
        this.valueAsString = value;
    }

    @JsonValue
    public String getValue() {
        return valueAsString;
    }

    public TargetType fromString(String value){
        TargetType result = null;
        for(TargetType type : values()){
            if(value.equals(type.valueAsString)) {
                result = type;
            }
        }

        if(result == null){
            throw new IllegalArgumentException("No such target type: " + value);
        }

        return result;
    }
}
