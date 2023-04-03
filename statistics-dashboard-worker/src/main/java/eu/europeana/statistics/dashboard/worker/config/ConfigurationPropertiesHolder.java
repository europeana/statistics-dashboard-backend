package eu.europeana.statistics.dashboard.worker.config;

import eu.europeana.metis.mongo.connection.MongoProperties;
import eu.europeana.metis.solr.connection.SolrProperties;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Class that is used to read all configuration properties for the application.
 * <p>
 * It uses {@link PropertySource} to identify the properties on application startup
 * </p>
 */
@Component
public class ConfigurationPropertiesHolder {

  //Mongo Metis Core
  @Value("${mongo.core.hosts}")
  private String[] mongoCoreHosts;
  @Value("${mongo.core.port}")
  private int[] mongoCorePorts;
  @Value("${mongo.core.username}")
  private String mongoCoreUsername;
  @Value("${mongo.core.password}")
  private String mongoCorePassword;
  @Value("${mongo.core.authentication.db}")
  private String mongoCoreAuthenticationDatabase;
  @Value("${mongo.core.db}")
  private String mongoCoreDatabase;
  @Value("${mongo.core.enable.ssl}")
  private boolean mongoCoreEnableSsl;
  @Value("${mongo.core.application.name}")
  private String mongoCoreApplicationName;

  //Mongo Metis Statistics Dashboard
  @Value("${mongo.sd.hosts}")
  private String[] mongoSDHosts;
  @Value("${mongo.sd.port}")
  private int[] mongoSDPorts;
  @Value("${mongo.sd.username}")
  private String mongoSDUsername;
  @Value("${mongo.sd.password}")
  private String mongoSDPassword;
  @Value("${mongo.sd.authentication.db}")
  private String mongoSDAuthenticationDatabase;
  @Value("${mongo.sd.db}")
  private String mongoSDDatabase;
  @Value("${mongo.sd.enable.ssl}")
  private boolean mongoSDEnableSsl;
  @Value("${mongo.sd.application.name}")
  private String mongoSDApplicationName;

  //Custom truststore
  @Value("${truststore.path}")
  private String truststorePath;
  @Value("${truststore.password}")
  private String truststorePassword;

  // Solr/Zookeeper publish
  @Value("${solr.publish.hosts}")
  private String[] publishSolrHosts;
  @Value("${zookeeper.publish.hosts}")
  private String[] publishZookeeperHosts;
  @Value("${zookeeper.publish.port}")
  private int[] publishZookeeperPorts;
  @Value("${zookeeper.publish.chroot}")
  private String publishZookeeperChroot;
  @Value("${zookeeper.publish.defaultCollection}")
  private String publishZookeeperDefaultCollection;

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
