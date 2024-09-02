package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import java.util.List;

/**
 * Class that encapsulates all data related to a country
 */

public class Country {

    private List<CurrentTarget> currentTargetData;
    private List<HistoricalData> historicalData;

    public Country(List<CurrentTarget> currentTargetData, List<HistoricalData> historicalData) {
        this.currentTargetData = currentTargetData;
        this.historicalData = historicalData;
    }

    public List<CurrentTarget> getCurrentTargetData() {
        return currentTargetData;
    }

    public void setCurrentTargetData(List<CurrentTarget> currentTargetData) {
        this.currentTargetData = currentTargetData;
    }

    public List<HistoricalData> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<HistoricalData> historicalData) {
        this.historicalData = historicalData;
    }
}
