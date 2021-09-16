package eu.europeana.statistics.dashboard.rest.exception;

import eu.europeana.metis.exception.StructuredExceptionWrapper;
import eu.europeana.statistics.dashboard.service.exception.BreakdownDeclarationFailException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {@link ControllerAdvice} class that handles exceptions through spring.
 */
@ControllerAdvice
public class RestResponseExceptionHandler {


  /**
   * Handler for {@link BreakdownDeclarationFailException}.
   *
   * @param exception the exception thrown
   * @return {@link StructuredExceptionWrapper} a json friendly class that contains the error message for the client
   */
  @ExceptionHandler(BreakdownDeclarationFailException.class)
  @ResponseBody
  public StructuredExceptionWrapper facetDeclarationFailHandler(BreakdownDeclarationFailException exception) {
    return new StructuredExceptionWrapper(exception.getMessage());
  }

  /**
   * Handler for any {@link Exception}.
   *
   * @param exception the exception thrown
   * @return {@link StructuredExceptionWrapper} a json friendly class that contains the error message for the client
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public StructuredExceptionWrapper exceptionHandler(Exception exception) {
    return new StructuredExceptionWrapper(exception.getMessage());
  }
}
