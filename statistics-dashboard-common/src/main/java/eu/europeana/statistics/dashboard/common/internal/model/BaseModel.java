package eu.europeana.statistics.dashboard.common.internal.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import eu.europeana.metis.mongo.utils.ObjectIdSerializer;
import eu.europeana.statistics.dashboard.common.utils.TargetDataMongoFieldNames;
import org.bson.types.ObjectId;

/**
 * The type BaseModel.
 */
public class BaseModel {

  /**
   * The Id.
   */
  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  protected ObjectId id;

  /**
   * The Country.
   */
  @Property(TargetDataMongoFieldNames.COUNTRY_FIELD)
  protected String country;

  /**
   * The Three d.
   */
  @Property(TargetDataMongoFieldNames.THREE_D_FIELD)
  protected Long threeD;

  /**
   * The High quality.
   */
  @Property(TargetDataMongoFieldNames.HIGH_QUALITY_FIELD)
  protected Long highQuality;

  /**
   * The Total records.
   */
  @Property(TargetDataMongoFieldNames.TOTAL_RECORDS_FIELD)
  protected Long totalRecords;

  /**
   * Instantiates a new BaseModel.
   */
  protected BaseModel() {
    // no op
  }

  /**
   * Instantiates a new BaseModel.
   *
   * @param country the country
   * @param threeD the three d
   * @param highQuality the high quality
   * @param totalRecords the total records
   */
  protected BaseModel(String country, Long threeD, Long highQuality, Long totalRecords) {
    this.country = country;
    this.threeD = threeD;
    this.highQuality = highQuality;
    this.totalRecords = totalRecords;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public ObjectId getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(ObjectId id) {
    this.id = id;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets country.
   *
   * @param country the country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Gets three d.
   *
   * @return the three d
   */
  public Long getThreeD() {
    return threeD;
  }

  /**
   * Sets three d.
   *
   * @param threeD the three d
   */
  public void setThreeD(Long threeD) {
    this.threeD = threeD;
  }

  /**
   * Gets high quality.
   *
   * @return the high quality
   */
  public Long getHighQuality() {
    return highQuality;
  }

  /**
   * Sets high quality.
   *
   * @param highQuality the high quality
   */
  public void setHighQuality(Long highQuality) {
    this.highQuality = highQuality;
  }

  /**
   * Gets total records.
   *
   * @return the total records
   */
  public Long getTotalRecords() {
    return totalRecords;
  }

  /**
   * Sets total records.
   *
   * @param totalRecords the total records
   */
  public void setTotalRecords(Long totalRecords) {
    this.totalRecords = totalRecords;
  }
}
