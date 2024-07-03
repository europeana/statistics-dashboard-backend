package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.Target;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;

import eu.europeana.statistics.dashboard.service.persistence.MongoSDTargetsDao;
//import eu.europeana.statistics.dashboard.common.internal.TargetType;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryTargetService {

    private final MongoSDTargetsDao mongoSDTargetsDao;
    private final String[] targetTypes = new String[]{ "three_d", "hq", "total" };

    /**
     *
     * @param mongoSDTargetsDao - The mongo database it connects to
     */
    public CountryTargetService(MongoSDTargetsDao mongoSDTargetsDao) {
        this.mongoSDTargetsDao = mongoSDTargetsDao;
    }

    /**
     * @return all HistoricalCountryTargetData objects
     */
    public List<HistoricalCountryTargetData> getAllCountryData(){
      List<Historical> historicalData = mongoSDTargetsDao.getCountryTargetData();
      return historicalData.stream().map(data -> new HistoricalCountryTargetData(
          data.getCountry(),
          data.getTimestamp(),
          data.getThreeD(),
          data.getHighQuality(),
          data.getTotalRecords())
      ).toList();
    }

    /**
     * @return all HistoricalCountryTargetData objects
     */
    public List<CountryTargetResult> getCountryTargets(){

      List<Target> targetData = mongoSDTargetsDao.getCountryTargets();

      List<CountryTargetResult> result = new ArrayList<>();

      for (Target data : targetData) {
        String label = Integer.toString(data.getYear());
        String country = data.getCountry();
        int[] values = new int[]{ data.getThreeD(), data.getHighQuality(), data.getTotalRecords() };

        for (int i = 0; i < values.length; i++) {
          result.add(new CountryTargetResult(
              country,
              targetTypes[i],
              label,
              values[i]
          ));
        }
      }
      return result;
    }
}
