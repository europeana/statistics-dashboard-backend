package eu.europeana.statistics.dashboard.common.internal.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Property;
import eu.europeana.statistics.dashboard.common.utils.TargetDataMongoFieldNames;

/**
 * The type Target.
 */
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
public class Target extends BaseModel {

  @Property(TargetDataMongoFieldNames.TARGET_YEAR_FIELD)
  private int year;

  /**
   * Instantiates a new Target.
   */
  //Empty constructor for when we perform queries
  public Target() {
    super();
  }

  /**
   * Instantiates a new Target.
   *
   * @param country the country
   * @param threeD the three d
   * @param highQuality the high quality
   * @param totalRecords the total records
   * @param year the year
   */
  public Target(String country, Long threeD, Long highQuality, Long totalRecords, int year) {
    super(country, threeD, highQuality, totalRecords);
    this.year = year;
  }

  /**
   * Gets year.
   *
   * @return the year
   */
  public int getYear() {
    return year;
  }

  /**
   * Sets year.
   *
   * @param year the year
   */
  public void setYear(int year) {
    this.year = year;
  }
}
