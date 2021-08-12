package statistics.dashboard.common.models.count;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize
@JsonRootName(value = MetadataTier.FILTER_TYPE_NAME)
public class MetadataTier extends StatisticsCountFilter{

  static final String FILTER_TYPE_NAME = "metadataTier";

  public MetadataTier(Integer breakdown, List<String> values) {
    super(breakdown, values);
  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
