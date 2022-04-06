package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class that encapsulates all types of statistics filters, each of them with their conditions
 */

@JsonSerialize
public class StatisticsFilteringRequest {

  @JsonProperty(FilterNames.CONTENT_TIER)
  private StatisticsBreakdownValueFilter contentTier;
  @JsonProperty(FilterNames.CONTENT_TYPE)
  private StatisticsBreakdownValueFilter contentType;
  @JsonProperty(FilterNames.COUNTRY)
  private StatisticsBreakdownValueFilter country;
  @JsonProperty(FilterNames.DATA_PROVIDER)
  private StatisticsBreakdownValueFilter dataProvider;
  @JsonProperty(FilterNames.METADATA_TIER)
  private StatisticsBreakdownValueFilter metadataTier;
  @JsonProperty(FilterNames.PROVIDER)
  private StatisticsBreakdownValueFilter provider;
  @JsonProperty(FilterNames.RIGHTS_CATEGORY)
  private StatisticsBreakdownValueFilter rightsCategory;
  @JsonProperty(FilterNames.DATASET_ID)
  private StatisticsValueFilter datasetId;
  @JsonProperty(FilterNames.CREATED_DATE)
  private StatisticsRangeFilter createdDate;
  @JsonProperty(FilterNames.UPDATED_DATE)
  private StatisticsRangeFilter updatedDate;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsFilteringRequest() {
  }

  public StatisticsFilteringRequest(StatisticsBreakdownValueFilter contentTier,
      StatisticsBreakdownValueFilter contentType, StatisticsBreakdownValueFilter country,
      StatisticsBreakdownValueFilter dataProvider, StatisticsBreakdownValueFilter metadataTier,
      StatisticsBreakdownValueFilter provider, StatisticsBreakdownValueFilter rightsCategory,
      StatisticsValueFilter datasetId, StatisticsRangeFilter createdDate, StatisticsRangeFilter updatedDate) {
    this.contentTier = contentTier;
    this.contentType = contentType;
    this.country = country;
    this.dataProvider = dataProvider;
    this.metadataTier = metadataTier;
    this.provider = provider;
    this.rightsCategory = rightsCategory;
    this.datasetId = datasetId;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public StatisticsBreakdownValueFilter getContentTier() {
    return contentTier;
  }

  public void setContentTier(StatisticsBreakdownValueFilter contentTier) {
    this.contentTier = contentTier;
  }

  public StatisticsBreakdownValueFilter getContentType() {
    return contentType;
  }

  public void setContentType(StatisticsBreakdownValueFilter contentType) {
    this.contentType = contentType;
  }

  public StatisticsBreakdownValueFilter getCountry() {
    return country;
  }

  public void setCountry(StatisticsBreakdownValueFilter country) {
    this.country = country;
  }

  public StatisticsBreakdownValueFilter getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(StatisticsBreakdownValueFilter dataProvider) {
    this.dataProvider = dataProvider;
  }

  public StatisticsBreakdownValueFilter getMetadataTier() {
    return metadataTier;
  }

  public void setMetadataTier(StatisticsBreakdownValueFilter metadataTier) {
    this.metadataTier = metadataTier;
  }

  public StatisticsBreakdownValueFilter getProvider() {
    return provider;
  }

  public void setProvider(StatisticsBreakdownValueFilter provider) {
    this.provider = provider;
  }

  public StatisticsBreakdownValueFilter getRightsCategory() {
    return rightsCategory;
  }

  public void setRightsCategory(
      StatisticsBreakdownValueFilter rightsCategory) {
    this.rightsCategory = rightsCategory;
  }

  public StatisticsRangeFilter getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(StatisticsRangeFilter createdDate) {
    this.createdDate = createdDate;
  }

  public StatisticsRangeFilter getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(StatisticsRangeFilter updatedDate) {
    this.updatedDate = updatedDate;
  }

  public StatisticsValueFilter getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(StatisticsValueFilter datasetId) {
    this.datasetId = datasetId;
  }

  public List<StatisticsBreakdownValueFilter> getAllBreakdownValueFilters(){
    List<StatisticsBreakdownValueFilter> filters = new ArrayList<>();
    filters.add(contentTier);
    filters.add(contentType);
    filters.add(country);
    filters.add(dataProvider);
    filters.add(metadataTier);
    filters.add(provider);
    filters.add(rightsCategory);

    return filters.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  public List<StatisticsRangeFilter> getAllRangeFilters(){
    List<StatisticsRangeFilter> filters = new ArrayList<>();
    filters.add(createdDate);
    filters.add(updatedDate);

    return filters.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }
}
