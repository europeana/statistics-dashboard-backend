package eu.europeana.statistics.dashboard.common.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;

/**
 * Class that encapsulates all types of statistics filters, each of them with their conditions
 */

@JsonSerialize
public class StatisticsFilteringRequest {

  @JsonProperty(FilterNames.CONTENT_TIER)
  private StatisticsCountFilter contentTier;
  @JsonProperty(FilterNames.CONTENT_TYPE)
  private StatisticsCountFilter contentType;
  @JsonProperty(FilterNames.COUNTRY)
  private StatisticsCountFilter country;
  @JsonProperty(FilterNames.DATA_PROVIDER)
  private StatisticsCountFilter dataProvider;
  @JsonProperty(FilterNames.METADATA_TIER)
  private StatisticsCountFilter metadataTier;
  @JsonProperty(FilterNames.PROVIDER)
  private StatisticsCountFilter provider;
  @JsonProperty(FilterNames.RIGHTS_STATEMENTS)
  private StatisticsCountFilter rightsStatements;
  @JsonProperty(FilterNames.CREATED_DATE)
  private StatisticsRangeFilter createdDate;
  @JsonProperty(FilterNames.UPDATED_DATE)
  private StatisticsRangeFilter updatedDate;

  /**
   * This empty constructor is needed for deserialization
   */
  public StatisticsFilteringRequest() {
  }

  public StatisticsFilteringRequest(StatisticsCountFilter contentTier,
      StatisticsCountFilter contentType, StatisticsCountFilter country,
      StatisticsCountFilter dataProvider, StatisticsCountFilter metadataTier,
      StatisticsCountFilter provider, StatisticsCountFilter rightsStatements,
      StatisticsRangeFilter createdDate, StatisticsRangeFilter updatedDate) {
    this.contentTier = contentTier;
    this.contentType = contentType;
    this.country = country;
    this.dataProvider = dataProvider;
    this.metadataTier = metadataTier;
    this.provider = provider;
    this.rightsStatements = rightsStatements;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public StatisticsCountFilter getContentTier() {
    return contentTier;
  }

  public void setContentTier(StatisticsCountFilter contentTier) {
    this.contentTier = contentTier;
  }

  public StatisticsCountFilter getContentType() {
    return contentType;
  }

  public void setContentType(StatisticsCountFilter contentType) {
    this.contentType = contentType;
  }

  public StatisticsCountFilter getCountry() {
    return country;
  }

  public void setCountry(StatisticsCountFilter country) {
    this.country = country;
  }

  public StatisticsCountFilter getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(StatisticsCountFilter dataProvider) {
    this.dataProvider = dataProvider;
  }

  public StatisticsCountFilter getMetadataTier() {
    return metadataTier;
  }

  public void setMetadataTier(StatisticsCountFilter metadataTier) {
    this.metadataTier = metadataTier;
  }

  public StatisticsCountFilter getProvider() {
    return provider;
  }

  public void setProvider(StatisticsCountFilter provider) {
    this.provider = provider;
  }

  public StatisticsCountFilter getRightsStatements() {
    return rightsStatements;
  }

  public void setRightsStatements(
      StatisticsCountFilter rightsStatements) {
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
}
