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
    private int threeD;

    @Property(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD)
    private int highQuality;

    @Property(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD)
    private int totalRecords;

    @Property(TargetDataMongoFieldNames.TIMESTAMP_FIELD)
    private LocalDateTime timestamp;

    //Empty constructor for when we perform queries
    public HistoricalDataModel(){}

    public HistoricalDataModel(String country, int threeD, int highQuality, int totalRecords, LocalDateTime timestamp) {
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

    public int getThreeD() {
        return threeD;
    }

    public void setThreeD(int threeD) {
        this.threeD = threeD;
    }

    public int getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(int highQuality) {
        this.highQuality = highQuality;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
