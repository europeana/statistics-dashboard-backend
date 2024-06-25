package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import java.util.List;

/**
 * Class that encapsulates all overview data for each country
 */
public class OverviewData {

    private List<CountryOverview> overviewData;

    public OverviewData(List<CountryOverview> overviewData) {
        this.overviewData = overviewData;
    }

    public List<CountryOverview> getOverviewData() {
        return overviewData;
    }

    public void setOverviewData(List<CountryOverview> overviewData) {
        this.overviewData = overviewData;
    }
}
