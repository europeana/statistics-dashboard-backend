package eu.europeana.statistics.dashboard.service.exception;

public class FacetDeclarationFailException extends Exception {

  public FacetDeclarationFailException(String message) {
    super(message);
  }

  public FacetDeclarationFailException(String message, Throwable cause) {
    super(message, cause);
  }
}
