package eu.europeana.statistics.dashboard.common.iternal;

import com.fasterxml.jackson.annotation.JsonValue;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;

public enum FacetValue {
  CONTENT_TIER(FilterNames.CONTENT_TIER),
  CONTENT_TYPE(FilterNames.CONTENT_TYPE),
  COUNTRY(FilterNames.COUNTRY),
  DATA_PROVIDER(FilterNames.DATA_PROVIDER),
  METADATA_TIER(FilterNames.METADATA_TIER),
  PROVIDER(FilterNames.PROVIDER),
  RIGHTS_STATEMENTS(FilterNames.RIGHTS_STATEMENTS);

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
