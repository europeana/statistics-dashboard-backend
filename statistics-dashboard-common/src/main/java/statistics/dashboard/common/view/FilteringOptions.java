package statistics.dashboard.common.view;

import java.util.List;
import statistics.dashboard.common.models.range.CreatedDate;
import statistics.dashboard.common.models.range.UpdatedDate;

public class FilteringOptions {

  private List<String> contentTier;
  private List<String> country;
  private List<String> dataProvider;
  private List<String> metadataTier;
  private List<String> provider;
  private List<String> rights;
  private List<String> contentType;
  private CreatedDate createdDate;
  private UpdatedDate updatedDate;

  public FilteringOptions(List<String> contentTier, List<String> country,
      List<String> dataProvider, List<String> metadataTier, List<String> provider,
      List<String> rights, List<String> contentType) {
    this.contentTier = contentTier;
    this.country = country;
    this.dataProvider = dataProvider;
    this.metadataTier = metadataTier;
    this.provider = provider;
    this.rights = rights;
    this.contentType = contentType;
  }

  public List<String> getContentTier() {
    return contentTier;
  }

  public void setContentTier(List<String> contentTier) {
    this.contentTier = contentTier;
  }

  public List<String> getCountry() {
    return country;
  }

  public void setCountry(List<String> country) {
    this.country = country;
  }

  public List<String> getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(List<String> dataProvider) {
    this.dataProvider = dataProvider;
  }

  public List<String> getMetadataTier() {
    return metadataTier;
  }

  public void setMetadataTier(List<String> metadataTier) {
    this.metadataTier = metadataTier;
  }

  public List<String> getProvider() {
    return provider;
  }

  public void setProvider(List<String> provider) {
    this.provider = provider;
  }

  public List<String> getRights() {
    return rights;
  }

  public void setRights(List<String> rights) {
    this.rights = rights;
  }

  public List<String> getContentType() {
    return contentType;
  }

  public void setContentType(List<String> contentType) {
    this.contentType = contentType;
  }
}
