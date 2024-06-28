package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.Target;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetDataResult;

import eu.europeana.statistics.dashboard.service.persistence.MongoSDTargetsDao;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CountryTargetService {

    private final MongoSDTargetsDao mongoSDTargetsDao;
    private final String[] targetTypes = new String[]{ "three_d", "hq", "total" };
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryTargetService.class);

    /**
     *
     * @param mongoSDTargetsDao - The mongo database it connects to
     */
    public CountryTargetService(MongoSDTargetsDao mongoSDTargetsDao) {
        this.mongoSDTargetsDao = mongoSDTargetsDao;
    }

    public String[] getTargetTypes(){
      return targetTypes;
    }

    /**
     * @return all HistoricalCountryTargetDataResult objects
     */
    public List<HistoricalCountryTargetDataResult> getCountryDataAll(){
      List<Historical> historicalData = mongoSDTargetsDao.getCountryTargetData();

      LOGGER.info("getCountryDataAll().........");

      return historicalData.stream().map(data -> new HistoricalCountryTargetDataResult(
          data.getCountry(),
          data.getTimestamp(),
          data.getThreeD(),
          data.getHighQuality(),
          data.getTotalRecords())
      ).toList();
    }

    public List<Target> getCountryTargetsRaw(){
      return mongoSDTargetsDao.getCountryTargets();
    }

    /**
     * @return all HistoricalCountryTargetDataResult objects
     */
    public List<CountryTargetResult> getCountryTargets(){

      List<Target> targetData = mongoSDTargetsDao.getCountryTargets();

      ArrayList<CountryTargetResult> result = new ArrayList<>();

      for (Target data : targetData) {
        String label = Integer.toString(data.getYear());
        String country = data.getCountry();
        boolean interim = data.getYear() == 2025;
        int[] values = new int[]{ data.getThreeD(), data.getHighQuality(), data.getTotalRecords() };

        for (int i = 0; i < values.length; i++) {
          result.add(new CountryTargetResult(
              country,
              targetTypes[i],
              label,
              values[i],
              interim
          ));
        }
      }
      return result;
    }
}
