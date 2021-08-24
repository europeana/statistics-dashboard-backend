package eu.europeana.statistics.dashboard.worker;

import static eu.europeana.metis.core.common.DaoFieldNames.DATASET_ID;
import static eu.europeana.metis.core.common.DaoFieldNames.ID;

import com.mongodb.client.MongoClient;
import dev.morphia.aggregation.experimental.Aggregation;
import dev.morphia.aggregation.experimental.expressions.Expressions;
import dev.morphia.aggregation.experimental.stages.Projection;
import dev.morphia.annotations.Entity;
import eu.europeana.metis.core.dataset.Dataset;
import eu.europeana.metis.core.mongo.MorphiaDatastoreProvider;
import eu.europeana.metis.core.mongo.MorphiaDatastoreProviderImpl;
import eu.europeana.metis.network.ExternalRequestUtil;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Data access object for the Metis core Mongo.
 */
public class MongoCoreDao {

  private final MorphiaDatastoreProvider datastoreProvider;

  /**
   * Constructor.
   *
   * @param mongoClient The mongo client.
   * @param mongoDatabaseName The name of the database in the Mongo.
   */
  public MongoCoreDao(MongoClient mongoClient, String mongoDatabaseName) {
    this.datastoreProvider = new MorphiaDatastoreProviderImpl(mongoClient, mongoDatabaseName);
  }

  /**
   * Get the IDs of all datasets in one set.
   *
   * @return The set of IDs.
   */
  public Stream<String> getAllDatasetIds() {
    return ExternalRequestUtil.retryableExternalRequestForNetworkExceptions(() -> {

      // Create aggregation pipeline finding all datasets.
      final Aggregation<Dataset> pipeline = datastoreProvider
              .getDatastore().aggregate(Dataset.class);

      // The field name should be the field name in DatasetIdWrapper.
      final String datasetIdField = "datasetId";

      // Project the dataset ID to the right field name.
      pipeline.project(Projection.project().exclude(ID.getFieldName())
              .include(datasetIdField, Expressions.field(DATASET_ID.getFieldName())));

      // Perform the aggregation and add the IDs in the result set.
      final Iterator<DatasetIdWrapper> resultIterator = pipeline.execute(DatasetIdWrapper.class);
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(resultIterator, 0), false)
              .map(DatasetIdWrapper::getDatasetId);
    });
  }

  /**
   * A wrapper class for a dataset ID that is used in an aggregation query. The {@link Entity}
   * annotation is needed so that Morphia can handle the aggregation.
   */
  @Entity
  private static class DatasetIdWrapper {

    // Name depends on the mongo aggregations in which it is used.
    private String datasetId;

    String getDatasetId() {
      return datasetId;
    }
  }
}
