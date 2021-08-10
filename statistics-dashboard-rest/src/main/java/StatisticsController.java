import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StatisticsController {

  public static final String GENERAL_STATISTICS = "/statistics/europeana/general";
  public static final String FILTERING_STATISTICS = "statistics/filtering";
  public static final String APPLICATION_JSON = "application/json";

  @GetMapping(value = GENERAL_STATISTICS, consumes = {APPLICATION_JSON},
      produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void getGeneralStatistics(){

  }

  @PostMapping(value = FILTERING_STATISTICS, consumes = {APPLICATION_JSON},
      produces = {APPLICATION_JSON})
  @ResponseStatus(HttpStatus.OK)
  public void getFilters(@RequestBody String filters){

  }

}
