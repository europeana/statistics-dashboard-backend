package eu.europeana.statistics.dashboard.service.persistence;

import static eu.europeana.metis.network.ExternalRequestUtil.retryableExternalRequestForNetworkExceptions;

import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.NamingStrategy;
import dev.morphia.query.filters.Filters;
import eu.europeana.statistics.dashboard.common.internal.StatisticsRecordModel;
import eu.europeana.statistics.dashboard.common.internal.TargetDataModel;
import eu.europeana.statistics.dashboard.common.utils.MongoFieldNames;
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
  public void saveTargetRecords(List<TargetDataModel> records) {
    records.forEach(record -> record.setId(new ObjectId()));
    retryableExternalRequestForNetworkExceptions(() -> datastore.save(records));
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
