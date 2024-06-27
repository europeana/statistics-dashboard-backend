package eu.europeana.statistics.dashboard.common.api.response.targetdata.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class that encapsulates historical data for a specific date
 */
public class HistoricalData {

    private LocalDateTime timestamp;
    private List<Target> targets;

    public HistoricalData(LocalDateTime timestamp, List<Target> targets) {
        this.timestamp = timestamp;
        this.targets = targets;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Target> getTargetValues() {
        return targets;
    }

    public void setTargetValues(List<Target> targets) {
        this.targets = targets;
    }
}
