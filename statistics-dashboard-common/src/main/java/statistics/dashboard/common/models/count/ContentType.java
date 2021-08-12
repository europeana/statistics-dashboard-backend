package statistics.dashboard.common.models.count;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize
@JsonRootName(value = ContentType.FILTER_TYPE_NAME)
public class ContentType extends StatisticsCountFilter{

  static final String FILTER_TYPE_NAME = "contentType";

  public ContentType(Integer breakdown, List<String> values) {
    super(breakdown, values);
  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
