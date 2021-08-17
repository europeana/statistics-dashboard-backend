package eu.europeana.statistics.dashboard.worker;

import eu.europeana.metis.utils.CustomTruststoreAppender;
import eu.europeana.metis.utils.CustomTruststoreAppender.TrustStoreConfigurationException;
import eu.europeana.statistics.dashboard.worker.harvest.DataHarvestingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utilities class initializes the trust store and
 */
public final class TruststoreInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TruststoreInitializer.class);

  private TruststoreInitializer() {
    // This class should not be instantiated.
  }

  /**
   * Initialize the truststore.
   *
   * @param properties The properties.
   * @throws DataHarvestingException In case something went wrong.
   */
  public static void initializeTruststore(PropertiesHolder properties) throws DataHarvestingException {
    LOGGER.info("Append default truststore with custom truststore");
    if (StringUtils.isNotEmpty(properties.getTruststorePath()) && StringUtils
            .isNotEmpty(properties.getTruststorePassword())) {
      try {
        CustomTruststoreAppender.appendCustomTrustoreToDefault(properties.getTruststorePath(),
                properties.getTruststorePassword());
      } catch (TrustStoreConfigurationException e) {
        throw new DataHarvestingException(e.getMessage(), e);
      }
    }
  }
}
