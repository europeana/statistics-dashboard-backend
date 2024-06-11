package eu.europeana.statistics.dashboard.common.internal;

public enum TargetType {

    THREE_D("3D"),
    HIGH_QUALITY("High Quality"),
    TOTAL_RECORDS("Total Records"),;

    private final String valueAsString;

    TargetType(String value){
        this.valueAsString = value;
    }

    public String getValueAsString() {
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
