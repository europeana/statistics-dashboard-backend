package eu.europeana.statistics.dashboard.common.iternal;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FacetValue {
  CONTENT_TIER("contentTier"),
  CONTENT_TYPE("contentType"),
  COUNTRY("country"),
  DATA_PROVIDER("dataProvider"),
  METADATA_TIER("metadataTier"),
  PROVIDER("provider"),
  RIGHTS_STATEMENTS("rightsStatements");

  private final String name;

  FacetValue(String name){
    this.name = name;
  }

  public boolean equalValues(FacetValue otherValue){
    return name.equals(otherValue.name);
  }

  @JsonValue
  @Override
  public String toString(){
    return name;
  }
}
