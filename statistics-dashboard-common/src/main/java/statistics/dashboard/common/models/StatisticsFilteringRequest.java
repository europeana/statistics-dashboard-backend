package statistics.dashboard.common.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import statistics.dashboard.common.models.filters.StatisticsCountFilter;
import statistics.dashboard.common.models.filters.StatisticsRangeFilter;

@JsonSerialize
@ApiModel(value = "filters")
public class StatisticsFilteringRequest {

  private StatisticsCountFilter contentTier;
  private StatisticsCountFilter contentType;
  private StatisticsCountFilter country;
  private StatisticsCountFilter dataProvider;
  private StatisticsCountFilter metadataTier;
  private StatisticsCountFilter provider;
  private StatisticsCountFilter rightsStatements;
  private StatisticsRangeFilter createdDate;
  private StatisticsRangeFilter updatedDate;

  public StatisticsFilteringRequest() {
  }

  public StatisticsFilteringRequest(StatisticsCountFilter contentTier, StatisticsCountFilter contentType,
      StatisticsCountFilter country, StatisticsCountFilter dataProvider, StatisticsCountFilter metadataTier,
      StatisticsCountFilter provider, StatisticsCountFilter rightsStatements, StatisticsRangeFilter createdDate,
      StatisticsRangeFilter updatedDate) {
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
