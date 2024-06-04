package eu.europeana.statistics.dashboard.common.internal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.*;
import eu.europeana.metis.mongo.utils.ObjectIdSerializer;
import eu.europeana.statistics.dashboard.common.utils.TargetDataMongoFieldNames;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Entity
@Indexes({
        @Index(fields = {@Field(TargetDataMongoFieldNames.COUNTRY_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.THREE_D_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.TIMESTAMP_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.COUNTRY_FIELD),
                @Field(TargetDataMongoFieldNames.THREE_D_FIELD),
                @Field(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD),
                @Field(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD),
                @Field(TargetDataMongoFieldNames.TIMESTAMP_FIELD)})
})
public class HistoricalDataModel {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    @Property(TargetDataMongoFieldNames.COUNTRY_FIELD)
    private String country;

    @Property(TargetDataMongoFieldNames.THREE_D_FIELD)
    private Integer threeD;

    @Property(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD)
    private Integer highQuality;

    @Property(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD)
    private Integer totalRecords;

    @Property(TargetDataMongoFieldNames.TIMESTAMP_FIELD)
    private LocalDateTime timestamp;

    public HistoricalDataModel(String country, Integer threeD, Integer highQuality, Integer totalRecords, LocalDateTime timestamp) {
        this.country = country;
        this.threeD = threeD;
        this.highQuality = highQuality;
        this.totalRecords = totalRecords;
        this.timestamp = timestamp;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getThreeD() {
        return threeD;
    }

    public void setThreeD(Integer threeD) {
        this.threeD = threeD;
    }

    public Integer getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(Integer highQuality) {
        this.highQuality = highQuality;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
