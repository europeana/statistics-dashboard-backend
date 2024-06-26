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
import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;

public class ScriptsRunner implements CommandLineRunner {

    private final ConfigurationPropertiesHolder propertiesHolder;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptsRunner.class);

    public ScriptsRunner(ConfigurationPropertiesHolder propertiesHolder) {
        this.propertiesHolder = propertiesHolder;
    }

    @Override
    public void run(String... args) throws DataAccessConfigException, DataHarvestingException {

        final MongoClientProvider<DataAccessConfigException> mongoCoreClientProvider = new MongoClientProvider<>(propertiesHolder.getMongoCoreProperties());
        final MongoClientProvider<DataAccessConfigException> mongoSDClientProvider = new MongoClientProvider<>(propertiesHolder.getMongoSDProperties());
        final SolrClientProvider<DataAccessConfigException> solrClientProvider = new SolrClientProvider<>(propertiesHolder.getSolrProperties());

        try(final MongoClient mongoCoreClient = mongoCoreClientProvider.createMongoClient();
                final MongoClient mongoSDClient = mongoSDClientProvider.createMongoClient();
            final CompoundSolrClient solrClient = solrClientProvider.createSolrClient()){

            final MongoCoreDao mongoCoreDao = new MongoCoreDao(mongoCoreClient, propertiesHolder.getMongoCoreDatabase());
            // See https://github.com/spotbugs/spotbugs/issues/756
            @SuppressWarnings("findbugs:RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE") final SolrClient nativeSolrClient = solrClient.getSolrClient();
            MongoSDDao mongoSDDao = new MongoSDDao(mongoSDClient, propertiesHolder.getMongoSDDatabase(), true);

            //If there is *no* Target data collection, then run this script
            if(!mongoSDDao.doesCollectionDataExist("Target")){
                LOGGER.info("Starting target script execution");
                TargetDataRunner.runTargetDataScript(mongoSDDao);
                LOGGER.info("Finished target script execution");
            }

            LOGGER.info("Starting analyze data script execution");
            AnalyzerRunner.runAnalyzeScript(mongoCoreDao, nativeSolrClient, mongoSDDao);
            LOGGER.info("Finished analyze data script execution");

            LOGGER.info("Starting historical data script execution");
            HistoricalDataRunner.runHistoricalScript(mongoSDDao);
            LOGGER.info("Finished historical data script execution");

        } catch (IOException e) {
            throw new DataHarvestingException("Could not close mongo or solr client.", e);
        }

    }

}
