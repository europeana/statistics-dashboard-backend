package eu.europeana.statistics.dashboard.rest.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class is the bootstrap code for Spring. It tells Spring how to start this web application.
 */
public class StatisticsRestInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

  private static final int MAX_UPLOAD_SIZE_IN_MB = 5 * 1024 * 1024; // 5 MB

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[0];
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[]{StatisticsRestApplication.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

}
