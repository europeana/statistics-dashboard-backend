package eu.europeana.statistics.dashboard.service.persistence;

import static eu.europeana.metis.network.ExternalRequestUtil.retryableExternalRequestForNetworkExceptions;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.NamingStrategy;
import dev.morphia.query.Query;

import eu.europeana.statistics.dashboard.common.internal.model.Historical;
import eu.europeana.statistics.dashboard.common.internal.model.Target;

import eu.europeana.statistics.dashboard.common.api.response.targetdata.CountryTargetResult;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Data access object for the Statistics Dashboard Mongo.
 */
public class MongoSDTargetsDao {

  private final Datastore datastore;

  /**
   * Constructor.
   *
   * @param mongoClient       The mongo client.
   * @param mongoDatabaseName The name of the database in the Mongo.
   * @param createIndexes     The flag that initiates the database indexes
   */
  public MongoSDTargetsDao(MongoClient mongoClient, String mongoDatabaseName, boolean createIndexes) {
    final MapperOptions mapperOptions = MapperOptions.builder()
        .collectionNaming(NamingStrategy.identity()).build();
    this.datastore = Morphia.createDatastore(mongoClient, mongoDatabaseName, mapperOptions);
  }

  /**
   * Return all Historical entities
   */
  public List<Historical> getCountryTargetData(){
    List<Historical> queryResult = new ArrayList<>();
    Query<Historical> result = retryableExternalRequestForNetworkExceptions(() ->
            datastore.find(Historical.class));
    result.forEach(queryResult::add);
    return queryResult;
  }

  /**
   * Return all Target entities
   */
  public List<Target> getCountryTargets(){
    List<Target> queryResult = new ArrayList<>();
    Query<Target> result = retryableExternalRequestForNetworkExceptions(() ->
            datastore.find(Target.class));
    result.forEach(queryResult::add);
    return queryResult;
  }

}
