package eu.europeana.statistics.dashboard.worker;

import eu.europeana.metis.mongo.connection.MongoProperties;
import eu.europeana.metis.solr.connection.SolrProperties;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * Contains the properties required for the execution of the link checking functionality.
 */
public class PropertiesHolder {

  private static final String CONFIGURATION_FILE = "application.properties";

  // Mongo metis core
  private final String[] mongoCoreHosts;
  private final int[] mongoCorePorts;
  private final String mongoCoreAuthenticationDatabase;
  private final String mongoCoreUsername;
  private final String mongoCorePassword;
  private final boolean mongoCoreEnableSsl;
  private final String mongoCoreDatabase;
  private final String mongoCoreApplicationName;

  // Mongo statistics dashboard
  private final String[] mongoSDHosts;
  private final int[] mongoSDPorts;
  private final String mongoSDAuthenticationDatabase;
  private final String mongoSDUsername;
  private final String mongoSDPassword;
  private final boolean mongoSDEnableSsl;
  private final String mongoSDDatabase;
  private final String mongoSDApplicationName;

  // truststore
  private final String truststorePath;
  private final String truststorePassword;

  // Solr/Zookeeper publish
  private final String[] publishSolrHosts;
  private final String[] publishZookeeperHosts;
  private final int[] publishZookeeperPorts;
  private final String publishZookeeperChroot;
  private final String publishZookeeperDefaultCollection;

  /**
   * Constructor. Reads the property file and loads the properties.
   */
  public PropertiesHolder() {

    // Load properties file.
    final Properties properties = new Properties();
    try (final InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIGURATION_FILE)) {
      properties.load(stream);
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }

    // Mongo metis core
    mongoCoreHosts = properties.getProperty("mongo.core.hosts").split(",");
    mongoCorePorts = Arrays.stream(properties.getProperty("mongo.core.port").split(","))
        .mapToInt(Integer::parseInt).toArray();
    mongoCoreAuthenticationDatabase = properties.getProperty("mongo.core.authentication.db");
    mongoCoreUsername = properties.getProperty("mongo.core.username");
    mongoCorePassword = properties.getProperty("mongo.core.password");
    mongoCoreEnableSsl = Boolean.parseBoolean(properties.getProperty("mongo.core.enable.ssl"));
    mongoCoreDatabase = properties.getProperty("mongo.core.db");
    mongoCoreApplicationName = properties.getProperty("mongo.core.application.name");

    // Mongo statistics dashboard
    mongoSDHosts = properties.getProperty("mongo.sd.hosts").split(",");
    mongoSDPorts = Arrays.stream(properties.getProperty("mongo.sd.port").split(","))
        .mapToInt(Integer::parseInt).toArray();
    mongoSDAuthenticationDatabase = properties.getProperty("mongo.sd.authentication.db");
    mongoSDUsername = properties.getProperty("mongo.sd.username");
    mongoSDPassword = properties.getProperty("mongo.sd.password");
    mongoSDEnableSsl = Boolean.parseBoolean(properties.getProperty("mongo.sd.enable.ssl"));
    mongoSDDatabase = properties.getProperty("mongo.sd.db");
    mongoSDApplicationName = properties.getProperty("mongo.sd.application.name");

    // truststore
    truststorePath = properties.getProperty("truststore.path");
    truststorePassword = properties.getProperty("truststore.password");

    // Solr/Zookeeper publish
    publishSolrHosts = properties.getProperty("solr.publish.hosts").split(",");
    publishZookeeperHosts = properties.getProperty("zookeeper.publish.hosts").split(",");
    publishZookeeperPorts =
        Arrays.stream(properties.getProperty("zookeeper.publish.port").split(","))
            .mapToInt(Integer::parseInt).toArray();
    publishZookeeperChroot = properties.getProperty("zookeeper.publish.chroot");
    publishZookeeperDefaultCollection =
        properties.getProperty("zookeeper.publish.defaultCollection");
  }

  public String getTruststorePath() {
    return truststorePath;
  }

  public String getTruststorePassword() {
    return truststorePassword;
  }

  public MongoProperties<DataAccessConfigException> getMongoCoreProperties()
      throws DataAccessConfigException {
    final MongoProperties<DataAccessConfigException> properties =
        new MongoProperties<>(DataAccessConfigException::new);
    properties.setAllProperties(mongoCoreHosts, mongoCorePorts, mongoCoreAuthenticationDatabase,
        mongoCoreUsername, mongoCorePassword, mongoCoreEnableSsl, null, mongoCoreApplicationName);
    return properties;
  }

  public MongoProperties<DataAccessConfigException> getMongoSDProperties()
      throws DataAccessConfigException {
    final MongoProperties<DataAccessConfigException> properties =
        new MongoProperties<>(DataAccessConfigException::new);
    properties.setAllProperties(mongoSDHosts, mongoSDPorts, mongoSDAuthenticationDatabase,
        mongoSDUsername, mongoSDPassword, mongoSDEnableSsl, null, mongoSDApplicationName);
    return properties;
  }

  public SolrProperties<DataAccessConfigException> getSolrProperties()
      throws DataAccessConfigException {
    final SolrProperties<DataAccessConfigException> properties =
        new SolrProperties<>(DataAccessConfigException::new);
    properties.setZookeeperHosts(publishZookeeperHosts, publishZookeeperPorts);
    if (StringUtils.isNotBlank(publishZookeeperChroot)) {
      properties.setZookeeperChroot(publishZookeeperChroot);
    }
    if (StringUtils.isNotBlank(publishZookeeperDefaultCollection)) {
      properties.setZookeeperDefaultCollection(publishZookeeperDefaultCollection);
    }
    for (String host : publishSolrHosts) {
      try {
        properties.addSolrHost(new URI(host));
      } catch (URISyntaxException e) {
        throw new DataAccessConfigException(e.getMessage(), e);
      }
    }
    return properties;
  }

  public String getMongoSDDatabase() {
    return mongoSDDatabase;
  }

  public String getMongoCoreDatabase() {
    return mongoCoreDatabase;
  }
}
