package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

/**
 * Class that encapsulates target data for a specific country
 */
public class CountryTargetResult {

    private String country;
    private String targetType;
    private String label;
    private int value;
    private boolean interim;

    public CountryTargetResult(
     String country,
     String targetType,
     String label,
     int value,
     boolean interim
     ) {
        this.country = country;
        this.targetType = targetType;
        this.label = label;
        this.value = value;
        this.interim = interim;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean getInterim() {
        return interim;
    }

    public void setInterim(boolean interim) {
        this.interim = interim;
    }

}
