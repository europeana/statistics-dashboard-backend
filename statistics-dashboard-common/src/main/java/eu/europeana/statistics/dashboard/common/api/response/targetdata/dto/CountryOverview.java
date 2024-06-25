package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import java.util.List;

/**
 * Class that has an overview of the current data for each target type for a country
 */
public class CountryOverview {

    private String country;
    private List<Target> targetData;

    public CountryOverview(String country, List<Target> targetData) {
        this.country = country;
        this.targetData = targetData;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Target> getTargetData() {
        return targetData;
    }

    public void setTargetData(List<Target> targetData) {
        this.targetData = targetData;
    }
}
