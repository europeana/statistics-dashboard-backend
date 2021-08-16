package eu.europeana.statistics.dashboard.worker;

public class SDWorkerException extends Exception {

  public SDWorkerException(String message) {
    super(message);
  }

  public SDWorkerException(String message, Throwable cause) {
    super(message, cause);
  }
}
