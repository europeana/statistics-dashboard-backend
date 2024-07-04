package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Class that encapsulates historical data for a specific date
 */
public class HistoricalCountryTargetData {

    private LocalDateTime date;
    private String country;

    @JsonProperty("three_d")
    private int threeD;

    @JsonProperty("high_quality")
    private int highQuality;

    @JsonProperty("total")
    private int totalNumberRecords;

    public HistoricalCountryTargetData(
     String country,
     LocalDateTime date,
     int threeD,
     int highQuality,
     int totalNumberRecords
     ) {
        this.country = country;
        this.date = date;
        this.threeD = threeD;
        this.highQuality = highQuality;
        this.totalNumberRecords = totalNumberRecords;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getThreeD() {
        return threeD;
    }

    public void setThreeD(Integer threeD) {
        this.threeD = threeD;
    }

    public int getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(Integer highQuality) {
        this.highQuality = highQuality;
    }

    public int getTotalNumberRecords() {
        return totalNumberRecords;
    }

    public void setTotalNumberRecords(Integer totalNumberRecords) {
        this.totalNumberRecords = totalNumberRecords;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
