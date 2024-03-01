package eu.europeana.statistics.dashboard.rest.config;

import com.mongodb.client.MongoClient;
import eu.europeana.metis.mongo.connection.MongoClientProvider;
import eu.europeana.metis.mongo.connection.MongoProperties;
import eu.europeana.metis.utils.CustomTruststoreAppender;
import eu.europeana.metis.utils.CustomTruststoreAppender.TrustStoreConfigurationException;
import eu.europeana.metis.utils.apm.ElasticAPMConfiguration;
import eu.europeana.statistics.dashboard.service.StatisticsService;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The web application making available the repository functionality. This provides all the
 * configuration and is the starting point for all injections and beans. It also performs the
 * required setup.
 */
@Configuration
@EnableWebMvc
@Import({ElasticAPMConfiguration.class})
@ComponentScan(basePackages = {"eu.europeana.statistics.dashboard.rest.controller",
        "eu.europeana.statistics.dashboard.rest.exception"})
@EnableScheduling
public class ApplicationConfiguration implements WebMvcConfigurer{

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);

  private final ConfigurationPropertiesHolder properties;
  private final MongoClient mongoClient;
  private StatisticsService statisticsService;

  /**
   * Constructor.
   *
   * @param properties The properties.
   */
  @Autowired
  public ApplicationConfiguration(ConfigurationPropertiesHolder properties) throws TrustStoreConfigurationException {
    this.mongoClient = ApplicationConfiguration.initializeApplication(properties);
    this.properties = properties;
  }

  /**
   * This method performs the initializing tasks for the application.
   *
   * @param propertiesHolder The properties.
   * @return The Mongo client that can be used to access the mongo database.
   * @throws TrustStoreConfigurationException In case a problem occurred with the truststore.
   */
  static MongoClient initializeApplication(ConfigurationPropertiesHolder propertiesHolder)
          throws TrustStoreConfigurationException {

    // Set the truststore.
    LOGGER.info("Append default truststore with custom truststore");
    if (StringUtils.isNotEmpty(propertiesHolder.getTruststorePath())
            && StringUtils.isNotEmpty(propertiesHolder.getTruststorePassword())) {
      CustomTruststoreAppender.appendCustomTruststoreToDefault(propertiesHolder.getTruststorePath(),
              propertiesHolder.getTruststorePassword());
    }

    // Create the mongo connection
    LOGGER.info("Creating Mongo connection");
    final MongoProperties<IllegalArgumentException> mongoProperties = new MongoProperties<>(
            IllegalArgumentException::new);
    mongoProperties
            .setAllProperties(propertiesHolder.getMongoHosts(), new int[]{propertiesHolder.getMongoPort()},
                    propertiesHolder.getMongoAuthenticationDb(), propertiesHolder.getMongoUsername(),
                    propertiesHolder.getMongoPassword(), propertiesHolder.isMongoEnableSsl(),
                    null, propertiesHolder.getMongoApplicationName());

    return new MongoClientProvider<>(mongoProperties).createMongoClient();
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedOrigins(properties.getAllowedCorsHosts());
  }

  @Bean
  public MongoSDDao getMongoSDDao(){
    return new MongoSDDao(mongoClient, properties.getMongoDatabaseName(), false);
  }

  @Bean
  public StatisticsService getStatisticsService(MongoSDDao mongoSDDao){
    this.statisticsService = new StatisticsService(mongoSDDao);
    return statisticsService;
  }

  /**
   * Scheduled method that refreshes the rights url - category mapping.
   */
  @Scheduled(cron = "0 0 7 * * *") //every day at 7 o'clock
  public void refreshRightsUrlCategoryMapping() {
    statisticsService.refreshRightsUrlsCategoryMapping();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/swagger-ui/index.html");
  }

  /**
   * Closes any connections previous acquired.
   */
  @PreDestroy
  public void close() {
    if (mongoClient != null) {
      mongoClient.close();
    }
  }
}
