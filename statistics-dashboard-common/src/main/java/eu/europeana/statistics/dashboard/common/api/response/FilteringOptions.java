package eu.europeana.statistics.dashboard.common.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;

/**
 * Class that contains the available options after filtering the results
 */

public class FilteringOptions {

  @JsonProperty(FilterNames.CONTENT_TIER)
  private List<String> contentTier;
  @JsonProperty(FilterNames.COUNTRY)
  private List<String> country;
  @JsonProperty(FilterNames.DATA_PROVIDER)
  private List<String> dataProvider;
  @JsonProperty(FilterNames.METADATA_TIER)
  private List<String> metadataTier;
  @JsonProperty(FilterNames.PROVIDER)
  private List<String> provider;
  @JsonProperty(FilterNames.RIGHTS_STATEMENTS)
  private List<String> rights;
  @JsonProperty(FilterNames.CONTENT_TYPE)
  private List<String> contentType;
  @JsonProperty(FilterNames.CREATED_DATE)
  private StatisticsRangeFilter createdDate;
  @JsonProperty(FilterNames.UPDATED_DATE)
  private StatisticsRangeFilter updatedDate;

  public FilteringOptions(List<String> contentTier, List<String> country,
      List<String> dataProvider, List<String> metadataTier, List<String> provider,
      List<String> rights, List<String> contentType, StatisticsRangeFilter createdDate,
      StatisticsRangeFilter updatedDate) {
    this.contentTier = new ArrayList<>(contentTier);
    this.country = new ArrayList<>(country);
    this.dataProvider = new ArrayList<>(dataProvider);
    this.metadataTier = new ArrayList<>(metadataTier);
    this.provider = new ArrayList<>(provider);
    this.rights = new ArrayList<>(rights);
    this.contentType = new ArrayList<>(contentType);
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public List<String> getContentTier() {
    return Collections.unmodifiableList(contentTier);
  }

  public void setContentTier(List<String> contentTier) {
    this.contentTier = new ArrayList<>(contentTier);
  }

  public List<String> getCountry() {
    return Collections.unmodifiableList(country);
  }

  public void setCountry(List<String> country) {
    this.country = new ArrayList<>(country);
  }

  public List<String> getDataProvider() {
    return Collections.unmodifiableList(dataProvider);
  }

  public void setDataProvider(List<String> dataProvider) {
    this.dataProvider = new ArrayList<>(dataProvider);
  }

  public List<String> getMetadataTier() {
    return Collections.unmodifiableList(metadataTier);
  }

  public void setMetadataTier(List<String> metadataTier) {
    this.metadataTier = new ArrayList<>(metadataTier);
  }

  public List<String> getProvider() {
    return Collections.unmodifiableList(provider);
  }

  public void setProvider(List<String> provider) {
    this.provider = new ArrayList<>(provider);
  }

  public List<String> getRights() {
    return Collections.unmodifiableList(rights);
  }

  public void setRights(List<String> rights) {
    this.rights = new ArrayList<>(rights);
  }

  public List<String> getContentType() {
    return Collections.unmodifiableList(contentType);
  }

  public void setContentType(List<String> contentType) {
    this.contentType = new ArrayList<>(contentType);
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
