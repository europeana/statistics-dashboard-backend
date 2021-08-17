package eu.europeana.statistics.dashboard.worker.harvest;

public class DataHarvestingException extends Exception {

  public DataHarvestingException(String message) {
    super(message);
  }

  public DataHarvestingException(String message, Throwable cause) {
    super(message, cause);
  }
}
