package eu.europeana.statistics.dashboard.common.api.response.targetdata;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class that encapsulates historical data for a specific date
 */
public class HistoricalCountryTargetDataResult {

    private LocalDateTime date;
    private String country;

    private int three_d;
    private int hq;
    private int total;

    public HistoricalCountryTargetDataResult(
     String country,
     LocalDateTime date,
     int three_d,
     int hq,
     int total
     ) {
        this.country = country;
        this.date = date;
        this.three_d = three_d;
        this.hq = hq;
        this.total = total;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getThree_d() {
        return three_d;
    }

    public void setTHREE_D(Integer three_d) {
        this.three_d = three_d;
    }

    public int getHQ() {
        return hq;
    }

    public void setHQ(Integer hq) {
        this.hq = hq;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
