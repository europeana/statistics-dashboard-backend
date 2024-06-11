package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class that encapsulates historical data for a specific date
 */
public class HistoricalDataResult {

    private LocalDateTime timestamp;
    private List<TargetValue> targetValues;

    public HistoricalDataResult(LocalDateTime timestamp, List<TargetValue> targetValues) {
        this.timestamp = timestamp;
        this.targetValues = targetValues;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<TargetValue> getTargetValues() {
        return targetValues;
    }

    public void setTargetValues(List<TargetValue> targetValues) {
        this.targetValues = targetValues;
    }
}
