package eu.europeana.statistics.dashboard.service.exception;

public class FailedFieldException extends Exception {

  public FailedFieldException(String message) {
    super(message);
  }

  public FailedFieldException(String message, Throwable cause) {
    super(message, cause);
  }
}
