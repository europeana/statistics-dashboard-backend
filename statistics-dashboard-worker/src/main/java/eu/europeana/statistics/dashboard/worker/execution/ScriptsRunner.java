package eu.europeana.statistics.dashboard.worker.execution;

import com.mongodb.client.MongoClient;
import eu.europeana.metis.mongo.connection.MongoClientProvider;
import eu.europeana.metis.solr.client.CompoundSolrClient;
import eu.europeana.metis.solr.connection.SolrClientProvider;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import eu.europeana.statistics.dashboard.worker.config.ConfigurationPropertiesHolder;
import eu.europeana.statistics.dashboard.worker.config.DataAccessConfigException;
import eu.europeana.statistics.dashboard.worker.config.MongoCoreDao;
import eu.europeana.statistics.dashboard.worker.harvest.DataHarvestingException;
import java.io.IOException;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;

/**
 * A runner that encapsulates a set of runners and runs them in order.
 */
public final class ScriptsRunner implements CommandLineRunner {

  private final ConfigurationPropertiesHolder propertiesHolder;
  private final ResourceLoader resourceLoader;

  /**
   * Constructor.
   *
   * @param propertiesHolder the properties holder
   * @param resourceLoader the resource loader
   */
  public ScriptsRunner(ConfigurationPropertiesHolder propertiesHolder, ResourceLoader resourceLoader) {
    this.propertiesHolder = propertiesHolder;
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void run(String... args) throws DataAccessConfigException, DataHarvestingException {
    final MongoClientProvider<DataAccessConfigException> mongoCoreClientProvider =
        new MongoClientProvider<>(propertiesHolder.getMongoCoreProperties());
    final MongoClientProvider<DataAccessConfigException> mongoSDClientProvider =
        new MongoClientProvider<>(propertiesHolder.getMongoSDProperties());
    final SolrClientProvider<DataAccessConfigException> solrClientProvider =
        new SolrClientProvider<>(propertiesHolder.getSolrProperties());

    try (
        final MongoClient mongoCoreClient = mongoCoreClientProvider.createMongoClient();
        final MongoClient mongoSDClient = mongoSDClientProvider.createMongoClient();
        final CompoundSolrClient solrClient = solrClientProvider.createSolrClient()
    ) {
      final MongoCoreDao mongoCoreDao = new MongoCoreDao(mongoCoreClient, propertiesHolder.getMongoCoreDatabase());
      // See https://github.com/spotbugs/spotbugs/issues/756
      @SuppressWarnings("findbugs:RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
      final SolrClient nativeSolrClient = solrClient.getSolrClient();
      MongoSDDao mongoSDDao = new MongoSDDao(mongoSDClient, propertiesHolder.getMongoSDDatabase(), true);

      new TargetDataRunner(mongoSDDao, resourceLoader).run();
      new AnalyzerRunner(mongoCoreDao, nativeSolrClient, mongoSDDao).run();
      new HistoricalDataRunner(mongoSDDao).run();

    } catch (IOException e) {
      throw new DataHarvestingException("Could not close mongo or solr client.", e);
    }

  }

}
