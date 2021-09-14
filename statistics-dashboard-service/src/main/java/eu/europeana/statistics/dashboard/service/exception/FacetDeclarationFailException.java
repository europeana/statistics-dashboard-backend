package eu.europeana.statistics.dashboard.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Facet declaration failed")
public class FacetDeclarationFailException extends Exception {

  public FacetDeclarationFailException(String message) {
    super(message);
  }

  public FacetDeclarationFailException(String message, Throwable cause) {
    super(message, cause);
  }
}
