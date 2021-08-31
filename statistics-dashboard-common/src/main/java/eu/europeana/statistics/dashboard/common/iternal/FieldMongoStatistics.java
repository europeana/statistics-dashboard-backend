package eu.europeana.statistics.dashboard.common.iternal;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsCountFilter;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;

import eu.europeana.statistics.dashboard.common.api.response.FilteringOptions;
import eu.europeana.statistics.dashboard.common.utils.MongoFieldNames;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * This enum contains the different fields that can be queried in StatisticsQuery object.
 */
public enum FieldMongoStatistics {

  DATASET_ID(MongoFieldNames.DATASET_ID_FIELD, null, null, null, null),
  TYPE(MongoFieldNames.TYPE_FIELD, StatisticsFilteringRequest::getContentType, null, FilteringOptions::setContentType, null),
  COUNTRY(MongoFieldNames.COUNTRY_FIELD, StatisticsFilteringRequest::getCountry, null, FilteringOptions::setCountry, null),
  PROVIDER(MongoFieldNames.PROVIDER_FIELD, StatisticsFilteringRequest::getProvider, null, FilteringOptions::setProvider, null),
  DATA_PROVIDER(MongoFieldNames.DATA_PROVIDER_FIELD, StatisticsFilteringRequest::getDataProvider, null, FilteringOptions::setDataProvider, null),
  RIGHTS(MongoFieldNames.RIGHTS_FIELD, StatisticsFilteringRequest::getRightsStatements, null, FilteringOptions::setRights, null),
  CONTENT_TIER(MongoFieldNames.CONTENT_TIER_FIELD, StatisticsFilteringRequest::getContentTier, null, FilteringOptions::setContentTier, null),
  METADATA_TIER(MongoFieldNames.METADATA_TIER_FIELD, StatisticsFilteringRequest::getMetadataTier, null, FilteringOptions::setMetadataTier, null),
  CREATED_DATE(MongoFieldNames.CREATED_DATE_FIELD, null, StatisticsFilteringRequest::getCreatedDate, null, FilteringOptions::setCreatedDate),
  UPDATED_DATE(MongoFieldNames.UPDATED_DATE_FIELD, null, StatisticsFilteringRequest::getUpdatedDate, null, FilteringOptions::setUpdatedDate);

  private final String fieldName;
  private final Function<StatisticsFilteringRequest, StatisticsCountFilter> valueFilterGetter;
  private final Function<StatisticsFilteringRequest, StatisticsRangeFilter> rangeFilterGetter;
  private final BiConsumer<FilteringOptions, Set<String>> valueFilterSetter;
  private final BiConsumer<FilteringOptions, StatisticsRangeFilter> rangeFilterSetter;

  FieldMongoStatistics(String fieldName,
      Function<StatisticsFilteringRequest, StatisticsCountFilter> valueFilterGetter,
      Function<StatisticsFilteringRequest, StatisticsRangeFilter> rangeFilterGetter,
      BiConsumer<FilteringOptions, Set<String>> valueFilterSetter,
      BiConsumer<FilteringOptions, StatisticsRangeFilter> rangeFilterSetter) {
    this.fieldName = fieldName;
    this.valueFilterGetter = valueFilterGetter;
    this.rangeFilterGetter = rangeFilterGetter;
    this.valueFilterSetter = valueFilterSetter;
    this.rangeFilterSetter = rangeFilterSetter;
  }

  /**
   * Obtain the field name as used in the statistics dashboard database.
   *
   * @return The field name.
   */
  public String getFieldName() {
    return fieldName;
  }

  public Function<StatisticsFilteringRequest, StatisticsCountFilter> getValueFilterGetter() {
    return valueFilterGetter;
  }

  public Function<StatisticsFilteringRequest, StatisticsRangeFilter> getRangeFilterGetter() {
    return rangeFilterGetter;
  }

  public BiConsumer<FilteringOptions, Set<String>> getValueFilterSetter() {
    return valueFilterSetter;
  }

  public BiConsumer<FilteringOptions, StatisticsRangeFilter> getRangeFilterSetter() {
    return rangeFilterSetter;
  }

  public static Set<FieldMongoStatistics> getValueFields(){
    Set<FieldMongoStatistics> result = new HashSet<>();
    result.add(FieldMongoStatistics.TYPE);
    result.add(FieldMongoStatistics.COUNTRY);
    result.add(FieldMongoStatistics.PROVIDER);
    result.add(FieldMongoStatistics.DATA_PROVIDER);
    result.add(FieldMongoStatistics.RIGHTS);
    result.add(FieldMongoStatistics.CONTENT_TIER);
    result.add(FieldMongoStatistics.METADATA_TIER);
    return result;
  }

  public static Set<FieldMongoStatistics> getRangeFields(){
    Set<FieldMongoStatistics> result = new HashSet<>();
    result.add(FieldMongoStatistics.CREATED_DATE);
    result.add(FieldMongoStatistics.UPDATED_DATE);
    return result;
  }
}
