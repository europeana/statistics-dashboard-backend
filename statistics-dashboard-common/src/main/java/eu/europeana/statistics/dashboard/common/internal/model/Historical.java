package eu.europeana.statistics.dashboard.common.internal.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Property;
import eu.europeana.statistics.dashboard.common.utils.TargetDataMongoFieldNames;
import java.time.LocalDateTime;

/**
 * The type Historical.
 */
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
public class Historical extends BaseModel {

  @Property(TargetDataMongoFieldNames.TIMESTAMP_FIELD)
  private LocalDateTime timestamp;

  /**
   * Instantiates a new Historical.
   */
  //Empty constructor for when we perform queries
  public Historical() {
    super();
  }

  /**
   * Instantiates a new Historical.
   *
   * @param country the country
   * @param threeD the three d
   * @param highQuality the high quality
   * @param totalRecords the total records
   * @param timestamp the timestamp
   */
  public Historical(String country, Long threeD, Long highQuality, Long totalRecords, LocalDateTime timestamp) {
    super(country, threeD, highQuality, totalRecords);
    this.timestamp = timestamp;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Sets timestamp.
   *
   * @param timestamp the timestamp
   */
  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
