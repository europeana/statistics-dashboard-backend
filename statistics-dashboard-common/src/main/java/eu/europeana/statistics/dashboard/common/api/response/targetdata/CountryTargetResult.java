package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import eu.europeana.statistics.dashboard.common.internal.TargetType;

/**
 * Class that encapsulates target data for a specific country
 */
public class CountryTargetResult {

    private String country;
    private TargetType targetType;
    private int targetYear;
    private Long value;

    /**
     * constructor
     * @param country - the country
     * @param targetType - the TargetType
     * @param targetYear - the targetYear
     * @param value - the value
     */
    public CountryTargetResult(
     String country,
     TargetType targetType,
     int targetYear,
     Long value
     ) {
        this.country = country;
        this.targetType = targetType;
        this.targetYear = targetYear;
        this.value = value;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public int getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(int targetYear) {
        this.targetYear = targetYear;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
