package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

import eu.europeana.statistics.dashboard.common.internal.TargetType;
/*
  THREE_D("3D"),
  HIGH_QUALITY("High Quality"),
  TOTAL_RECORDS("Total Records"),;
*/

/**
 * Class that encapsulates target data for a specific country
 */
public class CountryTargetResult {

    private String country;

    private String targetType;
    //private TargetType targetType;

    private String label;
    private int value;

    public CountryTargetResult(
     String country,
     String targetType,
     String label,
     int value
     ) {
        this.country = country;
        this.targetType = targetType;
        this.label = label;
        this.value = value;
    }

    /*
    public CountryTargetResult(
     String country,
     TargetType targetType,
     String label,
     int value
     ) {
        this.country = country;
        this.targetType = targetType;
        this.label = label;
        this.value = value;
    }
    */

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTargetType() {
        return targetType;
    }

    /*
    public TargetType getTargetType() {
        return targetType;
    }
    */

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    /*
    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }
    */

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

}
