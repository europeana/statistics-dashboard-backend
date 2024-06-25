package eu.europeana.statistics.dashboard.service.persistence;

import static eu.europeana.metis.network.ExternalRequestUtil.retryableExternalRequestForNetworkExceptions;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.NamingStrategy;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.StatisticsRecordModel;
import eu.europeana.statistics.dashboard.common.internal.model.Target;
import eu.europeana.statistics.dashboard.common.utils.MongoFieldNames;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Data access object for the Statistics Dashboard Mongo.
 */
public class MongoSDDao {

  private final Datastore datastore;

  /**
   * Constructor.
   *
   * @param mongoClient       The mongo client.
   * @param mongoDatabaseName The name of the database in the Mongo.
   * @param createIndexes     The flag that initiates the database indexes
   */
  public MongoSDDao(MongoClient mongoClient, String mongoDatabaseName, boolean createIndexes) {
    final MapperOptions mapperOptions = MapperOptions.builder()
        .collectionNaming(NamingStrategy.identity()).build();
    this.datastore = Morphia.createDatastore(mongoClient, mongoDatabaseName, mapperOptions);
    this.datastore.getMapper().map(StatisticsRecordModel.class);
    if(createIndexes){
      datastore.ensureIndexes();
    }
  }

  /**
   * Delete all statistics for the given dataset ID.
   *
   * @param datasetId The dataset ID to clear the statistics for.
   */
  public void deleteRecords(String datasetId) {
    retryableExternalRequestForNetworkExceptions(() -> datastore.find(StatisticsRecordModel.class)
        .filter(Filters.eq(MongoFieldNames.DATASET_ID_FIELD, datasetId))
        .delete(new DeleteOptions().multi(true)));
  }

  /**
   * Saves the statistics to the database.
   *
   * @param records the statistics records.
   */
  public void saveRecords(List<StatisticsRecordModel> records) {
    records.forEach(record -> record.setId(new ObjectId()));
    retryableExternalRequestForNetworkExceptions(() -> datastore.save(records));
  }

  /**
   * Saves the target data to the database.
   *
   * @param records the target records.
   */
  public void saveTargetRecords(List<Target> records) {
    records.forEach(record -> record.setId(new ObjectId()));
    retryableExternalRequestForNetworkExceptions(() -> datastore.save(records));
  }

  /**
   * Saves a single historical data to the database.
   *
   * @param record the historical record.
   */
  public void saveHistoricalRecord(Historical record) {
    record.setId(new ObjectId());
    retryableExternalRequestForNetworkExceptions(() -> datastore.save(record));
  }

  /**
   * Returns all existing values of countries
   *
   * @return All existing values of countries
   */
  public List<String> getAllCountryValuesStatisticsModel() {
    ArrayList<String> countries = new ArrayList<>();
    DistinctIterable<String> docs = retryableExternalRequestForNetworkExceptions(() -> datastore
            .getCollection(StatisticsRecordModel.class).distinct("country", String.class));
    docs.forEach(countries::add);
    return countries;
  }

  /**
   * Returns target data of a given country
   *
   * @param country - The country to get the target data from
   * @return Target data of a given country
   */
  public List<Target> getAllTargetDataOfCountry(String country) {
    ArrayList<Target> queryResult = new ArrayList<>();
    Filter filter = Filters.eq("country", country);
    Query<Target> result = retryableExternalRequestForNetworkExceptions(() ->
            datastore.find(Target.class).filter(filter));
    result.forEach(queryResult::add);
    return queryResult;
  }

  /**
   * Returns all historical data of a given country
   *
   * @param country - The country to get the historical data from
   * @return The historical data of a given country
   */
  public List<Historical> getAllHistoricalOfCountry(String country){
    ArrayList<Historical> queryResult = new ArrayList<>();
    Filter filter = Filters.eq("country", country);
    Query<Historical> result = retryableExternalRequestForNetworkExceptions(() ->
            datastore.find(Historical.class).filter(filter));
    result.forEach(queryResult::add);
    return queryResult;
  }

  /**
   * Create a query for obtaining aggregated statistics.
   *
   * @return A query object.
   */
  public StatisticsQuery createStatisticsQuery() {
    return new StatisticsQuery(() -> datastore.aggregate(StatisticsRecordModel.class));
  }
}
