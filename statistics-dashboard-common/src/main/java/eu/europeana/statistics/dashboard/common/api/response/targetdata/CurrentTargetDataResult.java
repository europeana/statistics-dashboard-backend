package eu.europeana.statistics.dashboard.common.api.response.targetdata;

/**
 * Class that saves the current values for
 * a specific type of target data
 */
public class CurrentTargetDataResult {

    private String type;
    private Integer currentTotalRecords;
    private CurrentDataResult currentData;

    public CurrentTargetDataResult(String type, Integer currentTotalRecords, CurrentDataResult currentData) {
        this.type = type;
        this.currentTotalRecords = currentTotalRecords;
        this.currentData = currentData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCurrentTotalRecords() {
        return currentTotalRecords;
    }

    public void setCurrentTotalRecords(Integer currentTotalRecords) {
        this.currentTotalRecords = currentTotalRecords;
    }

    public CurrentDataResult getCurrentData() {
        return currentData;
    }

    public void setCurrentData(CurrentDataResult currentData) {
        this.currentData = currentData;
    }
}
