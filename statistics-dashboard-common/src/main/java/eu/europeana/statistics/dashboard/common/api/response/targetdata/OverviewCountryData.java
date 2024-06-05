package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

/**
 * Class that has an overview of the current data for each target type for a country
 */
public class OverviewCountryData {

    private String country;
    private List<TargetValues> targetData;

    public OverviewCountryData(String country, List<TargetValues> targetData) {
        this.country = country;
        this.targetData = targetData;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<TargetValues> getTargetData() {
        return targetData;
    }

    public void setTargetData(List<TargetValues> targetData) {
        this.targetData = targetData;
    }
}
