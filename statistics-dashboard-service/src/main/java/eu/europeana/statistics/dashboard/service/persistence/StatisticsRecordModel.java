package eu.europeana.statistics.dashboard.service.persistence;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Property;
import eu.europeana.metis.mongo.utils.ObjectIdSerializer;
import org.bson.types.ObjectId;

/**
 * This class models the database structure for statistics data.
 */
@Entity
@Indexes({
    @Index(fields = {@Field(StatisticsRecordModel.DATASET_ID_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.TYPE_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.COUNTRY_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.PROVIDER_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.DATA_PROVIDER_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.RIGHTS_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.CONTENT_TIER_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.METADATA_TIER_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.CREATED_DATE_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.UPDATED_DATE_FIELD)}),
    @Index(fields = {@Field(StatisticsRecordModel.DATASET_ID_FIELD),
        @Field(StatisticsRecordModel.TYPE_FIELD), @Field(StatisticsRecordModel.COUNTRY_FIELD),
        @Field(StatisticsRecordModel.PROVIDER_FIELD),
        @Field(StatisticsRecordModel.DATA_PROVIDER_FIELD),
        @Field(StatisticsRecordModel.RIGHTS_FIELD),
        @Field(StatisticsRecordModel.CONTENT_TIER_FIELD),
        @Field(StatisticsRecordModel.METADATA_TIER_FIELD),
        @Field(StatisticsRecordModel.CREATED_DATE_FIELD),
        @Field(StatisticsRecordModel.UPDATED_DATE_FIELD)},
        options = @IndexOptions(name = "all_fields_uniqueness", unique = true))
})
public class StatisticsRecordModel {

  static final String DATASET_ID_FIELD = "datasetId";
  static final String TYPE_FIELD = "type";
  static final String COUNTRY_FIELD = "country";
  static final String PROVIDER_FIELD = "provider";
  static final String DATA_PROVIDER_FIELD = "dataProvider";
  static final String RIGHTS_FIELD = "rights";
  static final String CONTENT_TIER_FIELD = "contentTier";
  static final String METADATA_TIER_FIELD = "metadataTier";
  static final String CREATED_DATE_FIELD = "createdDate";
  static final String UPDATED_DATE_FIELD = "updatedDate";
  static final String RECORD_COUNT_FIELD = "recordCount";

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  @Property(DATASET_ID_FIELD)
  private String datasetId;

  @Property(TYPE_FIELD)
  private String type;

  @Property(COUNTRY_FIELD)
  private String country;

  @Property(PROVIDER_FIELD)
  private String provider;

  @Property(DATA_PROVIDER_FIELD)
  private String dataProvider;

  @Property(RIGHTS_FIELD)
  private String rights;

  @Property(CONTENT_TIER_FIELD)
  private String contentTier;

  @Property(METADATA_TIER_FIELD)
  private String metadataTier;

  @Property(CREATED_DATE_FIELD)
  private String createdDate;

  @Property(UPDATED_DATE_FIELD)
  private String updatedDate;

  @Property(RECORD_COUNT_FIELD)
  private int recordCount;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(String dataProvider) {
    this.dataProvider = dataProvider;
  }

  public String getRights() {
    return rights;
  }

  public void setRights(String rights) {
    this.rights = rights;
  }

  public String getContentTier() {
    return contentTier;
  }

  public void setContentTier(String contentTier) {
    this.contentTier = contentTier;
  }

  public String getMetadataTier() {
    return metadataTier;
  }

  public void setMetadataTier(String metadataTier) {
    this.metadataTier = metadataTier;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  public int getRecordCount() {
    return recordCount;
  }

  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }
}
