package eu.europeana.statistics.dashboard.common.internal;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsFilteringRequest;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;

import eu.europeana.statistics.dashboard.common.api.request.StatisticsValueFilter;
import eu.europeana.statistics.dashboard.common.api.response.FilteringOptions;
import eu.europeana.statistics.dashboard.common.utils.MongoFieldNames;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * This enum contains the different fields that can be queried in StatisticsQuery object.
 */
public enum MongoStatisticsField {

  DATASET_ID(MongoFieldNames.DATASET_ID_FIELD, null, StatisticsFilteringRequest::getDatasetId, null, null, null),
  TYPE(MongoFieldNames.TYPE_FIELD, FacetValue.CONTENT_TYPE, StatisticsFilteringRequest::getContentType,
      null, FilteringOptions::setContentType, null),
  COUNTRY(MongoFieldNames.COUNTRY_FIELD, FacetValue.COUNTRY, StatisticsFilteringRequest::getCountry,
      null, FilteringOptions::setCountry, null),
  PROVIDER(MongoFieldNames.PROVIDER_FIELD, FacetValue.PROVIDER, StatisticsFilteringRequest::getProvider,
      null, FilteringOptions::setProvider, null),
  DATA_PROVIDER(MongoFieldNames.DATA_PROVIDER_FIELD, FacetValue.DATA_PROVIDER,
      StatisticsFilteringRequest::getDataProvider, null, FilteringOptions::setDataProvider, null),
  RIGHTS(MongoFieldNames.RIGHTS_FIELD, null, null, null, null, null),
  RIGHTS_CATEGORY(MongoFieldNames.RIGHTS_CATEGORY_FIELD, FacetValue.RIGHTS_CATEGORY,
      StatisticsFilteringRequest::getRightsCategory, null, FilteringOptions::setRightsCategory, null),
  CONTENT_TIER(MongoFieldNames.CONTENT_TIER_FIELD, FacetValue.CONTENT_TIER,
      StatisticsFilteringRequest::getContentTier, null, FilteringOptions::setContentTier, null),
  METADATA_TIER(MongoFieldNames.METADATA_TIER_FIELD, FacetValue.METADATA_TIER,
      StatisticsFilteringRequest::getMetadataTier, null, FilteringOptions::setMetadataTier, null),
  CREATED_DATE(MongoFieldNames.CREATED_DATE_FIELD, null, null,
      StatisticsFilteringRequest::getCreatedDate, null, FilteringOptions::setCreatedDate),
  UPDATED_DATE(MongoFieldNames.UPDATED_DATE_FIELD, null, null,
      StatisticsFilteringRequest::getUpdatedDate, null, FilteringOptions::setUpdatedDate);

  private final String fieldName;
  private final FacetValue facet;
  private final Function<StatisticsFilteringRequest, StatisticsValueFilter> valueFilterGetter;
  private final Function<StatisticsFilteringRequest, StatisticsRangeFilter> rangeFilterGetter;
  private final BiConsumer<FilteringOptions, Set<String>> valueFilterSetter;
  private final BiConsumer<FilteringOptions, StatisticsRangeFilter> rangeFilterSetter;

  MongoStatisticsField(String fieldName, FacetValue facet,
      Function<StatisticsFilteringRequest, StatisticsValueFilter> valueFilterGetter,
      Function<StatisticsFilteringRequest, StatisticsRangeFilter> rangeFilterGetter,
      BiConsumer<FilteringOptions, Set<String>> valueFilterSetter,
      BiConsumer<FilteringOptions, StatisticsRangeFilter> rangeFilterSetter) {
    this.fieldName = fieldName;
    this.facet = facet;
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

  public Function<StatisticsFilteringRequest, StatisticsValueFilter> getValueFilterGetter() {
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

  public FacetValue getFacet() {
    return facet;
  }

  public static Set<MongoStatisticsField> getValueFields() {
    Set<MongoStatisticsField> result = new HashSet<>();
    result.add(MongoStatisticsField.TYPE);
    result.add(MongoStatisticsField.COUNTRY);
    result.add(MongoStatisticsField.PROVIDER);
    result.add(MongoStatisticsField.DATA_PROVIDER);
    result.add(MongoStatisticsField.RIGHTS_CATEGORY);
    result.add(MongoStatisticsField.CONTENT_TIER);
    result.add(MongoStatisticsField.METADATA_TIER);
    return result;
  }

  public static Set<MongoStatisticsField> getRangeFields() {
    Set<MongoStatisticsField> result = new HashSet<>();
    result.add(MongoStatisticsField.CREATED_DATE);
    result.add(MongoStatisticsField.UPDATED_DATE);
    return result;
  }
}
