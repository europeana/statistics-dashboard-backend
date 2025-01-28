package eu.europeana.statistics.dashboard.worker.execution;

import eu.europeana.statistics.dashboard.common.internal.RightsCategory;
import eu.europeana.statistics.dashboard.worker.config.MongoCoreDao;
import eu.europeana.statistics.dashboard.worker.harvest.DataHarvestingException;
import eu.europeana.statistics.dashboard.worker.harvest.SolrHarvester;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.common.internal.model.StatisticsRecordModel;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * The main runner that analyzes all datasets and saves them in the statistics dashboard database.
 */
public final class AnalyzerRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final MongoCoreDao mongoCoreDao;
  private final SolrClient nativeSolrClient;
  private final MongoSDDao mongoSDDao;

  /**
   * Constructor.
   *
   * @param mongoCoreDao the mongo core dao
   * @param nativeSolrClient the native solr client
   * @param mongoSDDao the mongo sd dao
   */
  public AnalyzerRunner(MongoCoreDao mongoCoreDao, SolrClient nativeSolrClient, MongoSDDao mongoSDDao) {
    this.mongoCoreDao = mongoCoreDao;
    this.nativeSolrClient = nativeSolrClient;
    this.mongoSDDao = mongoSDDao;
  }

  @Override
  public void run(String... args) throws DataHarvestingException {
    LOGGER.info("Starting analyze data script execution");
    final Set<String> datasetIds = mongoCoreDao.getAllDatasetIds().collect(Collectors.toSet());
    analyzeDatasets(new SolrHarvester(nativeSolrClient), mongoSDDao, datasetIds);
    LOGGER.info("Finished analyze data script execution");
  }

  private void analyzeDatasets(SolrHarvester harvester, MongoSDDao mongoSDDao,
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
