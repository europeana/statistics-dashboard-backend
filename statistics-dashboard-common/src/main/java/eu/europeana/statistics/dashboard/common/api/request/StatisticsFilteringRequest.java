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
  private StatisticsValueFilter contentTier;
  @JsonProperty(FilterNames.CONTENT_TYPE)
  private StatisticsValueFilter contentType;
  @JsonProperty(FilterNames.COUNTRY)
  private StatisticsValueFilter country;
  @JsonProperty(FilterNames.DATA_PROVIDER)
  private StatisticsValueFilter dataProvider;
  @JsonProperty(FilterNames.METADATA_TIER)
  private StatisticsValueFilter metadataTier;
  @JsonProperty(FilterNames.PROVIDER)
  private StatisticsValueFilter provider;
  @JsonProperty(FilterNames.RIGHTS_STATEMENTS)
  private StatisticsValueFilter rightsStatements;
  @JsonProperty(FilterNames.DATASET_ID)
  private StatisticsNonBreakdownValueFilter datasetId;
  @JsonProperty(FilterNames.CREATED_DATE)
  private StatisticsRangeFilter createdDate;
  @JsonProperty(FilterNames.UPDATED_DATE)
  private StatisticsRangeFilter updatedDate;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsFilteringRequest() {
  }

  public StatisticsFilteringRequest(StatisticsValueFilter contentTier,
      StatisticsValueFilter contentType, StatisticsValueFilter country,
      StatisticsValueFilter dataProvider, StatisticsValueFilter metadataTier,
      StatisticsValueFilter provider, StatisticsValueFilter rightsStatements,
      StatisticsNonBreakdownValueFilter datasetId, StatisticsRangeFilter createdDate,
      StatisticsRangeFilter updatedDate) {
    this.contentTier = contentTier;
    this.contentType = contentType;
    this.country = country;
    this.dataProvider = dataProvider;
    this.metadataTier = metadataTier;
    this.provider = provider;
    this.rightsStatements = rightsStatements;
    this.datasetId = datasetId;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public StatisticsValueFilter getContentTier() {
    return contentTier;
  }

  public void setContentTier(StatisticsValueFilter contentTier) {
    this.contentTier = contentTier;
  }

  public StatisticsValueFilter getContentType() {
    return contentType;
  }

  public void setContentType(StatisticsValueFilter contentType) {
    this.contentType = contentType;
  }

  public StatisticsValueFilter getCountry() {
    return country;
  }

  public void setCountry(StatisticsValueFilter country) {
    this.country = country;
  }

  public StatisticsValueFilter getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(StatisticsValueFilter dataProvider) {
    this.dataProvider = dataProvider;
  }

  public StatisticsValueFilter getMetadataTier() {
    return metadataTier;
  }

  public void setMetadataTier(StatisticsValueFilter metadataTier) {
    this.metadataTier = metadataTier;
  }

  public StatisticsValueFilter getProvider() {
    return provider;
  }

  public void setProvider(StatisticsValueFilter provider) {
    this.provider = provider;
  }

  public StatisticsValueFilter getRightsStatements() {
    return rightsStatements;
  }

  public void setRightsStatements(
      StatisticsValueFilter rightsStatements) {
    this.rightsStatements = rightsStatements;
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

  public StatisticsNonBreakdownValueFilter getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(StatisticsNonBreakdownValueFilter datasetId) {
    this.datasetId = datasetId;
  }

  public List<StatisticsValueFilter> getAllValueFilters(){
    List<StatisticsValueFilter> filters = new ArrayList<>();
    filters.add(contentTier);
    filters.add(contentType);
    filters.add(country);
    filters.add(dataProvider);
    filters.add(metadataTier);
    filters.add(provider);
    filters.add(rightsStatements);

    return filters.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  public List<StatisticsRangeFilter> getAllRangeFilters(){
    List<StatisticsRangeFilter> filters = new ArrayList<>();
    filters.add(createdDate);
    filters.add(updatedDate);

    return filters.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }
}
