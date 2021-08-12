package statistics.dashboard.common.models.range;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;

@JsonSerialize
@JsonRootName(value = UpdatedDate.FILTER_TYPE_NAME)
public class UpdatedDate extends StatisticsRangeFilter{

  static final String FILTER_TYPE_NAME = "updatedDate";

  public UpdatedDate(Date from, Date to) {
    super(from, to);
  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
