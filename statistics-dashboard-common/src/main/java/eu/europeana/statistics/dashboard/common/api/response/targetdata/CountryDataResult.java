package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.util.List;

/**
 * Class that encapsulates all data related to a country
 */

public class CountryDataResult {

    private List<CurrentTargetDataResult> currentTargetData;
    private List<HistoricalDataResult> historicalData;

    public CountryDataResult(List<CurrentTargetDataResult> currentTargetData, List<HistoricalDataResult> historicalData) {
        this.currentTargetData = currentTargetData;
        this.historicalData = historicalData;
    }

    public List<CurrentTargetDataResult> getCurrentTargetData() {
        return currentTargetData;
    }

    public void setCurrentTargetData(List<CurrentTargetDataResult> currentTargetData) {
        this.currentTargetData = currentTargetData;
    }

    public List<HistoricalDataResult> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<HistoricalDataResult> historicalData) {
        this.historicalData = historicalData;
    }
}
