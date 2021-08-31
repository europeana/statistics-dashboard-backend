package eu.europeana.statistics.dashboard.common.iternal;

import com.fasterxml.jackson.annotation.JsonValue;
import eu.europeana.statistics.dashboard.common.utils.FilterNames;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public enum FacetValue {
  CONTENT_TIER(FilterNames.CONTENT_TIER),
  CONTENT_TYPE(FilterNames.CONTENT_TYPE),
  COUNTRY(FilterNames.COUNTRY),
  DATA_PROVIDER(FilterNames.DATA_PROVIDER),
  METADATA_TIER(FilterNames.METADATA_TIER),
  PROVIDER(FilterNames.PROVIDER),
  RIGHTS_STATEMENTS(FilterNames.RIGHTS_STATEMENTS);

  private final String name;

  FacetValue(String name) {
    this.name = name;
  }

  public boolean equalValues(FacetValue otherValue) {
    return name.equals(otherValue.name);
  }

  @JsonValue
  @Override
  public String toString() {
    return name;
  }

  public static FacetValue fromFieldToFacetValue(FieldMongoStatistics fieldMongoStatistics) {
    List<FacetValue> anyMatchOrContainsField = Arrays.stream(FacetValue.values())
        .filter(facetValue -> facetValue.toString().equals(fieldMongoStatistics.getFieldName()) ||
            facetValue.toString().toLowerCase(Locale.ROOT).contains(fieldMongoStatistics.getFieldName()))
        .collect(Collectors.toList());

    return Optional.ofNullable(anyMatchOrContainsField.get(0)).orElse(null);
  }
}
