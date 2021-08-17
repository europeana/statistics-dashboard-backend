package eu.europeana.statistics.dashboard.worker.persistence;

/**
 * This enum contains the different fields that can be queried.
 */
public enum Field {

  DATASET_ID(StatisticsRecordModel.DATASET_ID_FIELD),
  TYPE(StatisticsRecordModel.TYPE_FIELD),
  COUNTRY(StatisticsRecordModel.COUNTRY_FIELD),
  PROVIDER(StatisticsRecordModel.PROVIDER_FIELD),
  DATA_PROVIDER(StatisticsRecordModel.DATA_PROVIDER_FIELD),
  RIGHTS(StatisticsRecordModel.RIGHTS_FIELD),
  CONTENT_TIER(StatisticsRecordModel.CONTENT_TIER_FIELD),
  METADATA_TIER(StatisticsRecordModel.METADATA_TIER_FIELD),
  CREATED_DATE(StatisticsRecordModel.CREATED_DATE_FIELD),
  UPDATED_DATE(StatisticsRecordModel.UPDATED_DATE_FIELD);

  private final String fieldName;

  Field(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * Obtain the field name as used in the statistics dashboard database.
   *
   * @return The field name.
   */
  public String getFieldName() {
    return fieldName;
  }
}
