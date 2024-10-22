package eu.europeana.statistics.dashboard.common.internal.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Property;
import eu.europeana.metis.mongo.utils.ObjectIdSerializer;
import eu.europeana.statistics.dashboard.common.utils.MongoFieldNames;
import org.bson.types.ObjectId;

/**
 * This class models the database structure for statistics data.
 */
@Entity
@Indexes({
    @Index(fields = {@Field(MongoFieldNames.DATASET_ID_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.TYPE_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.COUNTRY_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.PROVIDER_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.DATA_PROVIDER_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.RIGHTS_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.RIGHTS_CATEGORY_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.CONTENT_TIER_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.METADATA_TIER_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.CREATED_DATE_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.UPDATED_DATE_FIELD)}),
    @Index(fields = {@Field(MongoFieldNames.DATASET_ID_FIELD),
        @Field(MongoFieldNames.TYPE_FIELD), @Field(MongoFieldNames.COUNTRY_FIELD),
        @Field(MongoFieldNames.PROVIDER_FIELD),
        @Field(MongoFieldNames.DATA_PROVIDER_FIELD),
        @Field(MongoFieldNames.RIGHTS_FIELD),
        @Field(MongoFieldNames.RIGHTS_CATEGORY_FIELD),
        @Field(MongoFieldNames.CONTENT_TIER_FIELD),
        @Field(MongoFieldNames.METADATA_TIER_FIELD),
        @Field(MongoFieldNames.CREATED_DATE_FIELD),
        @Field(MongoFieldNames.UPDATED_DATE_FIELD)},
        options = @IndexOptions(name = "all_fields_uniqueness", unique = true))
})
public class StatisticsRecordModel {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  @Property(MongoFieldNames.DATASET_ID_FIELD)
  private String datasetId;

  @Property(MongoFieldNames.TYPE_FIELD)
  private String type;

  @Property(MongoFieldNames.COUNTRY_FIELD)
  private String country;

  @Property(MongoFieldNames.PROVIDER_FIELD)
  private String provider;

  @Property(MongoFieldNames.DATA_PROVIDER_FIELD)
  private String dataProvider;

  @Property(MongoFieldNames.RIGHTS_FIELD)
  private String rights;

  @Property(MongoFieldNames.RIGHTS_CATEGORY_FIELD)
  private String rightsCategory;

  @Property(MongoFieldNames.CONTENT_TIER_FIELD)
  private String contentTier;

  @Property(MongoFieldNames.METADATA_TIER_FIELD)
  private String metadataTier;

  @Property(MongoFieldNames.CREATED_DATE_FIELD)
  private String createdDate;

  @Property(MongoFieldNames.UPDATED_DATE_FIELD)
  private String updatedDate;

  @Property(MongoFieldNames.RECORD_COUNT_FIELD)
  private Long recordCount;

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

  public String getRightsCategory() {
    return rightsCategory;
  }

  public void setRightsCategory(String rightsCategory) {
    this.rightsCategory = rightsCategory;
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

  public Long getRecordCount() {
    return recordCount;
  }

  public void setRecordCount(Long recordCount) {
    this.recordCount = recordCount;
  }
}
