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
    private String threeD;

    @Property(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD)
    private String highQuality;

    @Property(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD)
    private String totalRecords;

    @Property(TargetDataMongoFieldNames.TARGET_YEAR_FIELD)
    private String year;

    public TargetDataModel(String country, String threeD, String highQuality, String totalRecords, String year) {
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

    public String getThreeD() {
        return threeD;
    }

    public void setThreeD(String threeD) {
        this.threeD = threeD;
    }

    public String getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(String highQuality) {
        this.highQuality = highQuality;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
