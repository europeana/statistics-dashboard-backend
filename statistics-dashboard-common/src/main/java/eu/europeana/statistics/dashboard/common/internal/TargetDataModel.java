package eu.europeana.statistics.dashboard.common.internal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.*;
import eu.europeana.metis.mongo.utils.ObjectIdSerializer;
import eu.europeana.statistics.dashboard.common.utils.TargetDataMongoFieldNames;
import org.bson.types.ObjectId;

@Entity
@Indexes({
        @Index(fields = {@Field(TargetDataMongoFieldNames.COUNTRY_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.THREE_D_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.TARGET_YEAR_FIELD)}),
        @Index(fields = {@Field(TargetDataMongoFieldNames.COUNTRY_FIELD),
                @Field(TargetDataMongoFieldNames.THREE_D_FIELD),
                @Field(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD),
                @Field(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD),
                @Field(TargetDataMongoFieldNames.TARGET_YEAR_FIELD)})
})
public class TargetDataModel {
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

    @Property(TargetDataMongoFieldNames.TARGET_YEAR_FIELD)
    private int year;

    //Empty constructor for when we perform queries
    public TargetDataModel(){}

    public TargetDataModel(String country, int threeD, int highQuality, int totalRecords, int year) {
        this.country = country;
        this.threeD = threeD;
        this.highQuality = highQuality;
        this.totalRecords = totalRecords;
        this.year = year;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
