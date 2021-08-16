package eu.europeana.statistics.dashboard.worker;

import com.mongodb.client.MongoClient;
import eu.europeana.metis.mongo.connection.MongoClientProvider;
import eu.europeana.metis.solr.client.CompoundSolrClient;
import eu.europeana.metis.solr.connection.SolrClientProvider;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDatabaseMain {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateDatabaseMain.class);

  public static void main(String[] args)
      throws SDWorkerException, IOException, SolrServerException {

    // Read the properties and initialize
    final PropertiesHolder properties = new PropertiesHolder();
    TruststoreInitializer.initializeTruststore(properties);

    // Obtain all the database IDs.
    final MongoClientProvider<SDWorkerException> mongoCoreClientProvider = new MongoClientProvider<>(
        properties.getMongoCoreProperties());
    final Set<String> datasetIds;
    try (final MongoClient mongoCoreClient = mongoCoreClientProvider.createMongoClient()) {
      final MongoCoreDao mongoCoreDao = new MongoCoreDao(mongoCoreClient,
          properties.getMongoCoreDatabase());
      datasetIds = mongoCoreDao.getAllDatasetIds().collect(Collectors.toSet());
    }

    // Perform analysis on the SOLR.
    final SolrClientProvider<SDWorkerException> solrClientProvider = new SolrClientProvider<>(
        properties.getSolrProperties());
    final MongoClientProvider<SDWorkerException> mongoSDClientProvider = new MongoClientProvider<>(
        properties.getMongoSDProperties());
    try (
        final MongoClient mongoSDClient = mongoSDClientProvider.createMongoClient();
        final CompoundSolrClient solrClient = solrClientProvider.createSolrClient()) {
      // See https://github.com/spotbugs/spotbugs/issues/756
      @SuppressWarnings("findbugs:RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
      final SolrClient nativeSolrClient = solrClient.getSolrClient();
      analyzeDatasets(new SolrHarvester(nativeSolrClient),
          new MongoSDDao(mongoSDClient, properties.getMongoSDDatabase()), datasetIds);
    }
  }

  private static void analyzeDatasets(SolrHarvester harvester, MongoSDDao mongoSDDao,
      Set<String> datasetIds) throws IOException, SolrServerException {
    LOGGER.info("Checking {} datasets.\n", datasetIds.size());
    final AtomicInteger datasetCounter = new AtomicInteger(0);
    final AtomicInteger resultCounter = new AtomicInteger(0);
    for (String datasetId : datasetIds) {
      LOGGER.info("Analyzing dataset {} ({} of {}).", datasetId, datasetCounter.incrementAndGet(),
          datasetIds.size());
      final AtomicInteger resultCounterForThisDataset = new AtomicInteger(0);
      final List<StatisticsRecordModel> results = harvester.harvestDataset(datasetId);
      results.forEach(result -> {
        resultCounterForThisDataset.incrementAndGet();
        resultCounter.incrementAndGet();
      });
      LOGGER.info("{} records found.", resultCounterForThisDataset.get());
      mongoSDDao.deleteRecords(datasetId);
      mongoSDDao.saveRecords(results);
    }
    LOGGER.info("\nTOTAL: {} records found.", resultCounter.get());
  }
}