package statistics.dashboard.common.models.count;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import java.util.List;

@JsonSerialize
@JsonRootName(value = ContentTier.FILTER_TYPE_NAME)
@ApiModel(value = ContentTier.FILTER_TYPE_NAME)
public class ContentTier extends StatisticsCountFilter{

  static final String FILTER_TYPE_NAME = "contentTier";

  public ContentTier(int breakdown, List<String> values) {
    super(breakdown, values);

  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
