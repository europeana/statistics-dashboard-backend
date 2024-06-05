package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.time.LocalDateTime;

/**
 * Class that encapsulates historical data for a specific date
 */
public class HistoricalDataResult {

    private LocalDateTime timestamp;
    private TargetValues targetValues;

    public HistoricalDataResult(LocalDateTime timestamp, TargetValues targetValues) {
        this.timestamp = timestamp;
        this.targetValues = targetValues;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TargetValues getTargetValues() {
        return targetValues;
    }

    public void setTargetValues(TargetValues targetValues) {
        this.targetValues = targetValues;
    }
}
