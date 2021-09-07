package eu.europeana.statistics.dashboard.common.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;
import java.util.Collections;
import java.util.HashSet;
import eu.europeana.statistics.dashboard.common.api.request.StatisticsRangeFilter;
import java.util.Set;

/**
 * Class that contains the available options after filtering the results
 */

public class FilteringOptions {

  @JsonProperty(FilterNames.CONTENT_TIER)
  private Set<String> contentTier;
  @JsonProperty(FilterNames.COUNTRY)
  private Set<String> country;
  @JsonProperty(FilterNames.DATA_PROVIDER)
  private Set<String> dataProvider;
  @JsonProperty(FilterNames.METADATA_TIER)
  private Set<String> metadataTier;
  @JsonProperty(FilterNames.PROVIDER)
  private Set<String> provider;
  @JsonProperty(FilterNames.RIGHTS_STATEMENTS)
  private Set<String> rights;
  @JsonProperty(FilterNames.CONTENT_TYPE)
  private Set<String> contentType;
  @JsonProperty(FilterNames.CREATED_DATE)
  private StatisticsRangeFilter createdDate;
  @JsonProperty(FilterNames.UPDATED_DATE)
  private StatisticsRangeFilter updatedDate;

  public FilteringOptions() {}

  public FilteringOptions(Set<String> contentTier, Set<String> country,
      Set<String> dataProvider, Set<String> metadataTier, Set<String> provider,
      Set<String> rights, Set<String> contentType, StatisticsRangeFilter createdDate,
      StatisticsRangeFilter updatedDate) {
    this.contentTier = new HashSet<>(contentTier);
    this.country = new HashSet<>(country);
    this.dataProvider = new HashSet<>(dataProvider);
    this.metadataTier = new HashSet<>(metadataTier);
    this.provider = new HashSet<>(provider);
    this.rights = new HashSet<>(rights);
    this.contentType = new HashSet<>(contentType);
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public Set<String> getContentTier() {
    return Collections.unmodifiableSet(contentTier);
  }

  public void setContentTier(Set<String> contentTier) {
    this.contentTier = new HashSet<>(contentTier);
  }

  public Set<String> getCountry() {
    return Collections.unmodifiableSet(country);
  }

  public void setCountry(Set<String> country) {
    this.country = new HashSet<>(country);
  }

  public Set<String> getDataProvider() {
    return Collections.unmodifiableSet(dataProvider);
  }

  public void setDataProvider(Set<String> dataProvider) {
    this.dataProvider = new HashSet<>(dataProvider);
  }

  public Set<String> getMetadataTier() {
    return Collections.unmodifiableSet(metadataTier);
  }

  public void setMetadataTier(Set<String> metadataTier) {
    this.metadataTier = new HashSet<>(metadataTier);
  }

  public Set<String> getProvider() {
    return Collections.unmodifiableSet(provider);
  }

  public void setProvider(Set<String> provider) {
    this.provider = new HashSet<>(provider);
  }

  public Set<String> getRights() {
    return Collections.unmodifiableSet(rights);
  }

  public void setRights(Set<String> rights) {
    this.rights = new HashSet<>(rights);
  }

  public Set<String> getContentType() {
    return Collections.unmodifiableSet(contentType);
  }

  public void setContentType(Set<String> contentType) {
    this.contentType = new HashSet<>(contentType);
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
