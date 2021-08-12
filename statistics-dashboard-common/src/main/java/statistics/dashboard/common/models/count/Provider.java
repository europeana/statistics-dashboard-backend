package statistics.dashboard.common.models.count;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize
@JsonRootName(value = Provider.FILTER_TYPE_NAME)
public class Provider extends StatisticsCountFilter{

  static final String FILTER_TYPE_NAME = "provider";

  public Provider(Integer breakdown, List<String> values) {
    super(breakdown, values);
  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
