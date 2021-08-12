package statistics.dashboard.common.models.range;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;

@JsonSerialize
@JsonRootName(value = CreatedDate.FILTER_TYPE_NAME)
public class CreatedDate extends StatisticsRangeFilter{

  static final String FILTER_TYPE_NAME = "createdDate";

  public CreatedDate(Date from, Date to) {
    super(from, to);
  }

  @Override
  public String getFilterType() {
    return FILTER_TYPE_NAME;
  }
}
