package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

/**
 * Class that has an overview of the current data for each target type for a country
 */
public class OverviewCountryData {

    private String country;
    private List<TargetValue> targetData;

    public OverviewCountryData(String country, List<TargetValue> targetData) {
        this.country = country;
        this.targetData = targetData;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<TargetValue> getTargetData() {
        return targetData;
    }

    public void setTargetData(List<TargetValue> targetData) {
        this.targetData = targetData;
    }
}
