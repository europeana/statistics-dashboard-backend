package eu.europeana.statistics.dashboard.worker;

import com.mongodb.client.MongoClient;
import eu.europeana.metis.mongo.connection.MongoClientProvider;
import eu.europeana.metis.solr.client.CompoundSolrClient;
import eu.europeana.metis.solr.connection.SolrClientProvider;
import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import eu.europeana.statistics.dashboard.worker.harvest.DataHarvestingException;
import eu.europeana.statistics.dashboard.worker.harvest.SolrHarvester;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.common.internal.StatisticsRecordModel;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a script (main method) that harvests all datasets and save them in the
 * statistics dashboard database.
 */
public class CreateDatabaseMain {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateDatabaseMain.class);

  public static void main(String[] args) throws DataHarvestingException, DataAccessConfigException {

    // Read the properties and initialize
    final PropertiesHolder properties = new PropertiesHolder();
    TruststoreInitializer.initializeTruststore(properties);

    // Obtain all the database IDs.
    final MongoClientProvider<DataAccessConfigException> mongoCoreClientProvider = new MongoClientProvider<>(
        properties.getMongoCoreProperties());
    final Set<String> datasetIds;
    try (final MongoClient mongoCoreClient = mongoCoreClientProvider.createMongoClient()) {
      final MongoCoreDao mongoCoreDao = new MongoCoreDao(mongoCoreClient,
          properties.getMongoCoreDatabase());
      datasetIds = mongoCoreDao.getAllDatasetIds().collect(Collectors.toSet());
    }

    // Perform analysis on the SOLR.
    final SolrClientProvider<DataAccessConfigException> solrClientProvider = new SolrClientProvider<>(
        properties.getSolrProperties());
    final MongoClientProvider<DataAccessConfigException> mongoSDClientProvider = new MongoClientProvider<>(
        properties.getMongoSDProperties());
    try (
        final MongoClient mongoSDClient = mongoSDClientProvider.createMongoClient();
        final CompoundSolrClient solrClient = solrClientProvider.createSolrClient()) {
      // See https://github.com/spotbugs/spotbugs/issues/756
      @SuppressWarnings("findbugs:RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
      final SolrClient nativeSolrClient = solrClient.getSolrClient();
      analyzeDatasets(new SolrHarvester(nativeSolrClient),
          new MongoSDDao(mongoSDClient, properties.getMongoSDDatabase(), true), datasetIds);
    } catch (IOException e) {
      throw new DataHarvestingException("Could not close mongo or solr client.", e);
    }
  }

  private static void analyzeDatasets(SolrHarvester harvester, MongoSDDao mongoSDDao,
      Set<String> datasetIds) throws DataHarvestingException {
    LOGGER.info("Checking {} datasets.\n", datasetIds.size());
    final AtomicInteger datasetCounter = new AtomicInteger(0);
    final AtomicInteger resultCounter = new AtomicInteger(0);
    for (String datasetId : datasetIds) {
      LOGGER.info("Analyzing dataset {} ({} of {}).", datasetId, datasetCounter.incrementAndGet(),
          datasetIds.size());
      final AtomicInteger resultCounterForThisDataset = new AtomicInteger(0);
      final List<StatisticsRecordModel> results = harvester.harvestDataset(datasetId);
      results.forEach(result -> {
        result.setRightsCategory(RightsCategory.matchCategoryFromUrl(result.getRights()).getName());
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