package eu.europeana.statistics.dashboard.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Holder for the property values from Spring injection/property loading.
 */
@Component
@PropertySource({"classpath:statistics.properties"})
public class ApplicationProperties {

  // Socks proxy
  @Value("${socks.proxy.enabled}")
  private boolean socksProxyEnabled;
  @Value("${socks.proxy.host}")
  private String socksProxyHost;
  @Value("${socks.proxy.port}")
  private String socksProxyPort;
  @Value("${socks.proxy.username}")
  private String socksProxyUsername;
  @Value("${socks.proxy.password}")
  private String socksProxyPassword;

  // Mongo
  @Value("${mongo.sd.hosts}")
  private String[] mongoSDHosts;
  @Value("${mongo.sd.port}")
  private int mongoSDPort;
  @Value("${mongo.sd.authentication.db}")
  private String mongoSDAuthenticationDb;
  @Value("${mongo.sd.username}")
  private String mongoSDUsername;
  @Value("${mongo.sd.password}")
  private String mongoSDPassword;
  @Value("${mongo.sd.enable.ssl}")
  private boolean mongoSDEnableSsl;
  @Value("${mongo.sd.application.name}")
  private String mongoSDApplicationName;
  @Value("${mongo.sd.database.name}")
  private String mongoSDDatabaseName;

  // truststore
  @Value("${truststore.path}")
  private String truststorePath;
  @Value("${truststore.password}")
  private String truststorePassword;

  public boolean isSocksProxyEnabled() {
    return socksProxyEnabled;
  }

  public String getSocksProxyHost() {
    return socksProxyHost;
  }

  public String getSocksProxyPort() {
    return socksProxyPort;
  }

  public String getSocksProxyUsername() {
    return socksProxyUsername;
  }

  public String getSocksProxyPassword() {
    return socksProxyPassword;
  }

  public String[] getMongoSDHosts() {
    return mongoSDHosts.clone();
  }

  public int getMongoSDPort() {
    return mongoSDPort;
  }

  public String getMongoSDAuthenticationDb() {
    return mongoSDAuthenticationDb;
  }

  public String getMongoSDUsername() {
    return mongoSDUsername;
  }

  public String getMongoSDPassword() {
    return mongoSDPassword;
  }

  public boolean isMongoSDEnableSsl() {
    return mongoSDEnableSsl;
  }

  public String getMongoSDApplicationName() {
    return mongoSDApplicationName;
  }

  public String getMongoSDDatabaseName() {
    return mongoSDDatabaseName;
  }

  public String getTruststorePath() {
    return truststorePath;
  }

  public String getTruststorePassword() {
    return truststorePassword;
  }
}
