package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

/**
 * Class that encapsulates all overview data for each country
 */
public class OverviewDataResult {

    private List<OverviewDataResult> overviewData;

    public OverviewDataResult(List<OverviewDataResult> overviewData) {
        this.overviewData = overviewData;
    }

    public List<OverviewDataResult> getOverviewData() {
        return overviewData;
    }

    public void setOverviewData(List<OverviewDataResult> overviewData) {
        this.overviewData = overviewData;
    }
}
