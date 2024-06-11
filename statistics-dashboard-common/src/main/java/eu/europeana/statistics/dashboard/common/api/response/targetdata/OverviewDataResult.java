package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

/**
 * Class that encapsulates all overview data for each country
 */
public class OverviewDataResult {

    private List<OverviewCountryData> overviewData;

    public OverviewDataResult(List<OverviewCountryData> overviewData) {
        this.overviewData = overviewData;
    }

    public List<OverviewCountryData> getOverviewData() {
        return overviewData;
    }

    public void setOverviewData(List<OverviewCountryData> overviewData) {
        this.overviewData = overviewData;
    }
}
