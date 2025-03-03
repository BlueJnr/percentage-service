package com.tenpo.percentage.config.errors;

import com.tenpo.percentage.config.errors.enums.ErrorReason;
import com.tenpo.percentage.config.errors.model.ErrorResponse;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CustomRestExceptionHandler {

  private static final Logger log =
      org.slf4j.LoggerFactory.getLogger(CustomRestExceptionHandler.class);

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ErrorResponse handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String error =
        ex.getName()
            + " should be of type "
            + Objects.requireNonNull(ex.getRequiredType()).getName();

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_PARAMETER_TYPE_MISMATCH)
            .errors(Collections.singletonList(error))
            .build();

    logTrace(errorResponse, request);

    return errorResponse;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  public ErrorResponse handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(
          violation.getRootBeanClass().getName()
              + " "
              + violation.getPropertyPath()
              + ": "
              + violation.getMessage());
    }

    ErrorResponse errorResponse =
        ErrorResponse.builder().reason(ErrorReason.CONSTRAINT_VIOLATION).errors(errors).build();

    logTrace(errorResponse, request);

    return errorResponse;
  }

  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  @ExceptionHandler({RequestNotPermitted.class})
  public ErrorResponse handleRequestNotPermitted(
      ConstraintViolationException ex, WebRequest request) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .reason(ErrorReason.TOO_MANY_REQUESTS)
            .message(ex.getMessage())
            .build();

    logTrace(errorResponse, request);

    return errorResponse;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({Exception.class})
  public ErrorResponse handleDefault(Exception ex, WebRequest request) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .reason(ErrorReason.GENERIC_ERROR)
            .errors(Collections.singletonList(sw.toString()))
            .build();

    logTrace(errorResponse, request);

    return errorResponse;
  }

  private void logTrace(ErrorResponse errorResponse, WebRequest request) {
    log.error(
        "Failed {} request {}",
        ((ServletWebRequest) request).getHttpMethod(),
        request.getDescription(false));
    log.error(
        "Request parameters isEmpty={} : {}",
        request.getParameterMap().isEmpty(),
        request.getParameterMap());
    log.error("Http status error : {}", errorResponse.getReason().getHttpStatus());
    log.error("ErrorResponse : {}", errorResponse);
  }
}
