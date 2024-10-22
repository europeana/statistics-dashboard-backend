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
    private Long threeD;

    @JsonProperty("high_quality")
    private Long highQuality;

    @JsonProperty("total")
    private Long totalNumberRecords;

    /**
     * constructor
     * @param country - the country
     * @param date - the date
     * @param threeD - the threeD value
     * @param highQuality - the highQuality value
     * @param totalNumberRecords - the totalNumberRecords value
     */
    public HistoricalCountryTargetData(
     String country,
     LocalDateTime date,
     Long threeD,
     Long highQuality,
     Long totalNumberRecords
     ) {
        this.country = country;
        this.date = date;
        if(threeD > 0){
          this.threeD = threeD;
        }
        if(highQuality > 0){
          this.highQuality = highQuality;
        }
        if(totalNumberRecords > 0){
          this.totalNumberRecords = totalNumberRecords;
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getThreeD() {
        return threeD;
    }

    public void setThreeD(Long threeD) {
        this.threeD = threeD;
    }

    public Long getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(Long highQuality) {
        this.highQuality = highQuality;
    }

    public Long getTotalNumberRecords() {
        return totalNumberRecords;
    }

    public void setTotalNumberRecords(Long totalNumberRecords) {
        this.totalNumberRecords = totalNumberRecords;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
