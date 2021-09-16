package eu.europeana.statistics.dashboard.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Breakdown declaration failed")
public class BreakdownDeclarationFailException extends Exception {

  public BreakdownDeclarationFailException(String message) {
    super(message);
  }

  public BreakdownDeclarationFailException(String message, Throwable cause) {
    super(message, cause);
  }
}
