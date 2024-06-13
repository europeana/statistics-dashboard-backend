package eu.europeana.statistics.dashboard.service;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.*;
import eu.europeana.statistics.dashboard.common.internal.HistoricalDataModel;
import eu.europeana.statistics.dashboard.common.internal.MongoStatisticsField;
import eu.europeana.statistics.dashboard.common.internal.TargetDataModel;
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
    public CountryDataResult getCountryData(String country){
        return new CountryDataResult(prepareCountryCurrentTargetData(country), prepareHistoricalData(country));
    }

    /**
     * Returns an object encapsulating overview current data for all countries
     *
     * @return An object encapsulating all overview data for all countries
     */
    public OverviewDataResult getOverviewDataAllCountries(){
        List<String> countries = mongoSDDao.getAllCountryValuesStatisticsModel();
        List<OverviewCountryData> targetValues = countries.stream().map(this::prepareOverviewCountryData).toList();

        return new OverviewDataResult(targetValues);
    }


    private List<CurrentTargetDataResult> prepareCountryCurrentTargetData(String country){

        List<TargetDataModel> targetDataInfo = mongoSDDao.getAllTargetDataOfCountry(country);

        CurrentTargetDataResult current3DTargetDataResult = prepareCurrentTargetDataResult(TargetType.THREE_D, country, targetDataInfo);
        CurrentTargetDataResult currentHighQualityTargetDataResult = prepareCurrentTargetDataResult(TargetType.HIGH_QUALITY, country, targetDataInfo);
        CurrentTargetDataResult currentAllRecordsTargetDataResult = prepareCurrentTargetDataResult(TargetType.TOTAL_RECORDS, country, targetDataInfo);

        return List.of(current3DTargetDataResult, currentHighQualityTargetDataResult, currentAllRecordsTargetDataResult);

    }

    private CurrentTargetDataResult prepareCurrentTargetDataResult(TargetType targetType, String country, List<TargetDataModel> targetDataInfo){
        int currentValue = getCurrentValueOfTargetType(targetType, country);
        return new CurrentTargetDataResult(targetType, currentValue,
                List.of(prepareCurrentTargetData(currentValue, targetDataInfo.get(0), targetType),
                        prepareCurrentTargetData(currentValue, targetDataInfo.get(1), targetType)));
    }

    private CurrentDataResult prepareCurrentTargetData(int currentValue, TargetDataModel targetDataModel, TargetType targetType){

        return switch (targetType) {
            case THREE_D -> new CurrentDataResult(targetDataModel.getYear(), targetDataModel.getThreeD(),
                    calculatePercentage(targetDataModel.getThreeD(), currentValue));
            case HIGH_QUALITY -> new CurrentDataResult(targetDataModel.getYear(), targetDataModel.getHighQuality(),
                    calculatePercentage(targetDataModel.getHighQuality(), currentValue));
            case TOTAL_RECORDS -> new CurrentDataResult(targetDataModel.getYear(), targetDataModel.getTotalRecords(),
                    calculatePercentage(targetDataModel.getTotalRecords(), currentValue));
        };

    }

    private int getCurrentValueOfTargetType(TargetType targetType, String country){
        StatisticsQuery query = mongoSDDao.createStatisticsQuery();

        switch (targetType){
            case THREE_D:
                query.withValueFilter(MongoStatisticsField.TYPE, List.of(targetType.getValueAsString()))
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

    private List<HistoricalDataResult> prepareHistoricalData(String country){
        List<HistoricalDataModel> historicalData = mongoSDDao.getAllHistoricalOfCountry(country);
        return historicalData.stream().map(data -> new HistoricalDataResult(data.getTimestamp(), prepareTargetValuesList(data)))
                .toList();
    }

    private List<TargetValue> prepareTargetValuesList(HistoricalDataModel historicalDataModel){
        return List.of(new TargetValue(TargetType.THREE_D, historicalDataModel.getThreeD()),
                new TargetValue(TargetType.HIGH_QUALITY, historicalDataModel.getHighQuality()),
                new TargetValue(TargetType.TOTAL_RECORDS, historicalDataModel.getTotalRecords()));
    }

    private OverviewCountryData prepareOverviewCountryData(String country){
        int current3DValue = getCurrentValueOfTargetType(TargetType.THREE_D, country);
        int currentHighQualityValue = getCurrentValueOfTargetType(TargetType.HIGH_QUALITY, country);
        int currentTotalRecordsValue = getCurrentValueOfTargetType(TargetType.TOTAL_RECORDS, country);
        return new OverviewCountryData(country,
                List.of(new TargetValue(TargetType.THREE_D, current3DValue),
                        new TargetValue(TargetType.HIGH_QUALITY, currentHighQualityValue),
                        new TargetValue(TargetType.TOTAL_RECORDS, currentTotalRecordsValue)));
    }

    private double calculatePercentage(double totalCount, double count) {
        return totalCount <= 0 ? 0 : Double.parseDouble(PERCENTAGE_FORMAT.format((count / totalCount) * 100.0));
    }
}
