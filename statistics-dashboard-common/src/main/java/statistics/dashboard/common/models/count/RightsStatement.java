package statistics.dashboard.common.models.count;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize
@JsonRootName(value = RightsStatement.FILTER_TYPE_NAME)
public class RightsStatement extends StatisticsCountFilter{

  static final String FILTER_TYPE_NAME = "rightsStatements";

  public RightsStatement(Integer breakdown, List<String> values) {
    super(breakdown, values);
  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
