package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import eu.europeana.statistics.dashboard.common.internal.TargetType;

import java.util.List;

/**
 * Class that saves the current values for
 * a specific type of target data
 */
public class CurrentTarget {

    private TargetType type;
    private Integer currentTotalRecords;
    private List<CurrentData> currentData;

    public CurrentTarget(TargetType type, Integer currentTotalRecords, List<CurrentData> currentData) {
        this.type = type;
        this.currentTotalRecords = currentTotalRecords;
        this.currentData = currentData;
    }

    public TargetType getType() {
        return type;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public Integer getCurrentTotalRecords() {
        return currentTotalRecords;
    }

    public void setCurrentTotalRecords(Integer currentTotalRecords) {
        this.currentTotalRecords = currentTotalRecords;
    }

    public List<CurrentData> getCurrentData() {
        return currentData;
    }

    public void setCurrentData(List<CurrentData> currentData) {
        this.currentData = currentData;
    }
}
