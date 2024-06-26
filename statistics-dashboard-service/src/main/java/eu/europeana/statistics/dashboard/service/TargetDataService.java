package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.dto.*;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.common.internal.TargetType;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.service.persistence.StatisticsQuery;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Service
public class TargetDataService {

    private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));

    private final MongoSDDao mongoSDDao;

    /**
     * Constructor for the Statistics Server
     *
     * @param mongoSDDao - The mongo database it connects to
     */
    public TargetDataService(MongoSDDao mongoSDDao) {
        this.mongoSDDao = mongoSDDao;
    }

    /**
     * Get current values per target type and the historical data of given country
     * @param country - The country to get the information of
     * @return All target data and historical data associated with the given country
     */
    public Country getCountryData(String country){
        return new Country(prepareCountryCurrentTargetData(country), prepareHistoricalData(country));
    }

    /**
     * Returns an object encapsulating overview current data for all countries
     *
     * @return An object encapsulating all overview data for all countries
     */
    public OverviewData getOverviewDataAllCountries(){
        List<String> countries = mongoSDDao.getAllCountryValuesTargetCollection();
        List<CountryOverview> targetValues = countries.stream().map(this::prepareOverviewCountryData).toList();

        return new OverviewData(targetValues);
    }


    private List<CurrentTarget> prepareCountryCurrentTargetData(String country){

        List<eu.europeana.statistics.dashboard.common.internal.model.Target> targetDataInfo = mongoSDDao.getAllTargetDataOfCountry(country);

        CurrentTarget current3DTargetDataResult = prepareCurrentTargetDataResult(TargetType.THREE_D, country, targetDataInfo);
        CurrentTarget currentHighQualityTargetDataResult = prepareCurrentTargetDataResult(TargetType.HIGH_QUALITY, country, targetDataInfo);
        CurrentTarget currentAllRecordsTargetDataResult = prepareCurrentTargetDataResult(TargetType.TOTAL_RECORDS, country, targetDataInfo);

        return List.of(current3DTargetDataResult, currentHighQualityTargetDataResult, currentAllRecordsTargetDataResult);

    }

    private CurrentTarget prepareCurrentTargetDataResult(TargetType targetType, String country, List<eu.europeana.statistics.dashboard.common.internal.model.Target> targetDataInfo){
        int currentValue = getCurrentValueOfTargetType(targetType, country);
        return new CurrentTarget(targetType, currentValue,
                List.of(prepareCurrentTargetData(currentValue, targetDataInfo.get(0), targetType),
                        prepareCurrentTargetData(currentValue, targetDataInfo.get(1), targetType)));
    }

    private CurrentData prepareCurrentTargetData(int currentValue, eu.europeana.statistics.dashboard.common.internal.model.Target target, TargetType targetType){

        return switch (targetType) {
            case THREE_D -> new CurrentData(target.getYear(), target.getThreeD(),
                    calculatePercentage(target.getThreeD(), currentValue));
            case HIGH_QUALITY -> new CurrentData(target.getYear(), target.getHighQuality(),
                    calculatePercentage(target.getHighQuality(), currentValue));
            case TOTAL_RECORDS -> new CurrentData(target.getYear(), target.getTotalRecords(),
                    calculatePercentage(target.getTotalRecords(), currentValue));
        };

    }

    private int getCurrentValueOfTargetType(TargetType targetType, String country){
        StatisticsQuery query = mongoSDDao.createStatisticsQuery();

        switch (targetType){
            case THREE_D:
                query.withValueFilter(MongoStatisticsField.TYPE, List.of(targetType.getValue()))
                        .withValueFilter(MongoStatisticsField.COUNTRY, List.of(country));
                break;

            case HIGH_QUALITY:
                query.withValueFilter(MongoStatisticsField.CONTENT_TIER, List.of("2", "3", "4"))
                        .withValueFilter(MongoStatisticsField.COUNTRY, List.of(country));
                break;

            case TOTAL_RECORDS:
                query.withValueFilter(MongoStatisticsField.COUNTRY, List.of(country));
        }


        return query.queryForStatistics().getRecordCount();
    }

    private List<HistoricalData> prepareHistoricalData(String country){
        List<eu.europeana.statistics.dashboard.common.internal.model.Historical> historicalData = mongoSDDao.getAllHistoricalOfCountry(country);
        return historicalData.stream().map(data -> new HistoricalData(data.getTimestamp(), prepareTargetValuesList(data)))
                .toList();
    }

    private List<Target> prepareTargetValuesList(eu.europeana.statistics.dashboard.common.internal.model.Historical historical){
        return List.of(new Target(TargetType.THREE_D, historical.getThreeD()),
                new Target(TargetType.HIGH_QUALITY, historical.getHighQuality()),
                new Target(TargetType.TOTAL_RECORDS, historical.getTotalRecords()));
    }

    private CountryOverview prepareOverviewCountryData(String country){
        int current3DValue = getCurrentValueOfTargetType(TargetType.THREE_D, country);
        int currentHighQualityValue = getCurrentValueOfTargetType(TargetType.HIGH_QUALITY, country);
        int currentTotalRecordsValue = getCurrentValueOfTargetType(TargetType.TOTAL_RECORDS, country);
        return new CountryOverview(country,
                List.of(new Target(TargetType.THREE_D, current3DValue),
                        new Target(TargetType.HIGH_QUALITY, currentHighQualityValue),
                        new Target(TargetType.TOTAL_RECORDS, currentTotalRecordsValue)));
    }

    private double calculatePercentage(double totalCount, double count) {
        return totalCount <= 0 ? 0 : Double.parseDouble(PERCENTAGE_FORMAT.format((count / totalCount) * 100.0));
    }
}
