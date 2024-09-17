package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.Target;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;
import eu.europeana.statistics.dashboard.common.api.response.targetdata.HistoricalCountryTargetData;

import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.common.internal.TargetType;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for retrieving data pertaining to country targets
 */
@Service
public class CountryTargetService {

    private final MongoSDDao mongoSDDao;

    /**
     *
     * @param mongoSDDao - The mongo database it connects to
     */
    public CountryTargetService(MongoSDDao mongoSDDao) {
        this.mongoSDDao = mongoSDDao;
    }

    /**
     * getAllCountryDataFiltered
     * returns all HistoricalCountryTargetData objects for the country
     * @param country - the country to filter on
     * @return List<HistoricalCountryTargetData>
     */
    public List<HistoricalCountryTargetData> getAllCountryDataFiltered(String country){
      List<Historical> historicalData = mongoSDDao.getCountryTargetDataFiltered(country);
      return historicalData.stream().map(data -> new HistoricalCountryTargetData(
          data.getCountry(),
          data.getTimestamp(),
          data.getThreeD(),
          data.getHighQuality(),
          data.getTotalRecords())
      ).toList();
    }

    /**
     * getAllCountryDataLatest
     * returns the most recent HistoricalCountryTargetData for all countries
     * @return List<HistoricalCountryTargetData>
     */
    public List<HistoricalCountryTargetData> getAllCountryDataLatest(){

      List<HistoricalCountryTargetData> result = new ArrayList<>();
      List<String> countries = mongoSDDao.getAllCountryValuesTargetCollection();

      for(String country : countries) {
        Historical snapshot = mongoSDDao.generateLatestTargetData(country);
        result.add(
          new HistoricalCountryTargetData(country, snapshot.getTimestamp(), snapshot.getThreeD(),
        snapshot.getHighQuality(), snapshot.getTotalRecords())
        );
      }
      return result;
    }

    /**
     * getCountryTargets
     * returns all HistoricalCountryTargetData objects
     * @return List<CountryTargetResult>
     */
    public List<CountryTargetResult> getCountryTargets(){

      List<Target> targetData = mongoSDDao.getCountryTargets();
      List<CountryTargetResult> result = new ArrayList<>();

      for (Target data : targetData) {
        String country = data.getCountry();
        result.add(new CountryTargetResult(
            country,
            TargetType.THREE_D,
            data.getYear(),
            data.getThreeD()
        ));
        result.add(new CountryTargetResult(
            country,
            TargetType.HIGH_QUALITY,
            data.getYear(),
            data.getHighQuality()
        ));
        result.add(new CountryTargetResult(
            country,
            TargetType.TOTAL_RECORDS,
            data.getYear(),
            data.getTotalRecords()
        ));
      }
      return result;
    }
}
