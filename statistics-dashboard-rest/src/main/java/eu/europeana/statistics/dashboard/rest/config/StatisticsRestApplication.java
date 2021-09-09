package eu.europeana.statistics.dashboard.rest.config;

import com.mongodb.client.MongoClient;
import eu.europeana.corelib.web.socks.SocksProxy;
import eu.europeana.metis.mongo.connection.MongoClientProvider;
import eu.europeana.metis.mongo.connection.MongoProperties;
import eu.europeana.metis.utils.CustomTruststoreAppender;
import eu.europeana.metis.utils.CustomTruststoreAppender.TrustStoreConfigurationException;
import eu.europeana.statistics.dashboard.service.persistence.MongoSDDao;
import java.util.Collections;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The web application making available the repository functionality. This provides all the
 * configuration and is the starting point for all injections and beans. It also performs the
 * required setup.
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = {"eu.europeana.statistics.dashboard.rest",
    "eu.europeana.statistics.dashboard.service"})
public class StatisticsRestApplication implements WebMvcConfigurer, InitializingBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsRestApplication.class);

  private final ApplicationProperties properties;

  private MongoClient mongoClientSD;

  /**
   * Constructor.
   *
   * @param properties The properties.
   */
  @Autowired
  public StatisticsRestApplication(ApplicationProperties properties) {
    this.properties = properties;
  }

  @Override
  public void afterPropertiesSet() throws TrustStoreConfigurationException {

    // Set the SOCKS proxy
    if (properties.isSocksProxyEnabled()) {
      new SocksProxy(properties.getSocksProxyHost(), properties.getSocksProxyPort(),
              properties.getSocksProxyUsername(), properties.getSocksProxyPassword()).init();
    }

    // Set the truststore.
    LOGGER.info("Append default truststore with custom truststore");
    if (StringUtils.isNotEmpty(properties.getTruststorePath())
            && StringUtils.isNotEmpty(properties.getTruststorePassword())) {
      CustomTruststoreAppender.appendCustomTrustoreToDefault(properties.getTruststorePath(),
              properties.getTruststorePassword());
    }

    // Create the mongo connection
    LOGGER.info("Creating Mongo connection");
    final MongoProperties<IllegalArgumentException> mongoProperties = new MongoProperties<>(
            IllegalArgumentException::new);
    mongoProperties
            .setAllProperties(properties.getMongoHosts(), new int[]{properties.getMongoPort()},
                    properties.getMongoAuthenticationDb(), properties.getMongoUsername(),
                    properties.getMongoPassword(), properties.isMongoEnableSsl(),
                    null, properties.getMongoApplicationName());
    mongoClientSD = new MongoClientProvider<>(mongoProperties).createMongoClient();
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedOrigins(properties.getAllowedCorsHosts());
  }

  @Bean
  public MongoSDDao getMongoSDDao(){
    return new MongoSDDao(mongoClientSD, properties.getMongoDatabaseName());
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

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
            "Statistics REST API",
            "Statistics REST API",
            "v1",
            "API TOS",
            new Contact("development", "europeana.eu", "development@europeana.eu"),
            "EUPL Licence v1.2",
            "https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12",
            Collections.emptyList());
  }

  /**
   * Closes any connections previous acquired.
   */
  @PreDestroy
  public void close() {
    if (mongoClientSD != null) {
      mongoClientSD.close();
    }
  }
}
