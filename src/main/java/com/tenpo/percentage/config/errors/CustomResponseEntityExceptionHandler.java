package com.tenpo.percentage.config.errors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tenpo.percentage.config.errors.enums.ErrorReason;
import com.tenpo.percentage.config.errors.model.ErrorResponse;
import com.tenpo.percentage.config.exception.ApiException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_PARAMETER_NOT_VALID)
            .errors(
                getErrors(
                    ex.getBindingResult().getFieldErrors(),
                    ex.getBindingResult().getGlobalErrors()))
            .build(),
        ErrorReason.REQUEST_PARAMETER_NOT_VALID.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    String error = ex.getRequestPartName() + " parameter is missing";
    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_PARAMETER_MISSING)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.REQUEST_PARAMETER_MISSING.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    String error = ex.getParameterName() + " parameter is missing";
    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_PARAMETER_MISSING)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.REQUEST_PARAMETER_MISSING.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.NOT_FOUND)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.NOT_FOUND.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are: ");
    Objects.requireNonNull(ex.getSupportedHttpMethods())
        .forEach(t -> builder.append(t).append(" "));

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.METHOD_NOT_ALLOWED)
            .errors(Collections.singletonList(builder.toString()))
            .build(),
        ErrorReason.METHOD_NOT_ALLOWED.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.UNSUPPORTED_MEDIA_TYPE)
            .errors(Collections.singletonList(builder.toString()))
            .build(),
        ErrorReason.UNSUPPORTED_MEDIA_TYPE.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String error =
        ex.getPropertyName()
            + " should be of type "
            + Objects.requireNonNull(ex.getRequiredType()).getName();

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_PARAMETER_TYPE_MISMATCH)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.REQUEST_PARAMETER_TYPE_MISMATCH.getHttpStatus(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    final Throwable cause = ex.getCause();

    if (cause instanceof InvalidFormatException) {
      return this.handleInvalidFormatException((InvalidFormatException) cause, status, request);
    } else if (cause instanceof JsonParseException) {
      return this.handleJsonParseException((JsonParseException) cause, status, request);
    } else if (cause instanceof JsonMappingException) {
      return this.handleJsonMappingException((JsonMappingException) cause, status, request);
    }

    String error = "Uh oh, we received an empty request, please check again";

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_BODY_EMPTY)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.REQUEST_BODY_EMPTY.getHttpStatus(),
        request);
  }

  @ExceptionHandler({InvalidFormatException.class})
  public ResponseEntity<Object> handleInvalidFormatException(
      InvalidFormatException ex, HttpStatusCode status, WebRequest request) {
    String error =
        "The value '" + ex.getValue() + "' should be of type " + ex.getTargetType().getName();

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.REQUEST_BODY_INVALID_FORMAT)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.REQUEST_BODY_INVALID_FORMAT.getHttpStatus(),
        request);
  }

  @ExceptionHandler({JsonParseException.class})
  public ResponseEntity<Object> handleJsonParseException(
      JsonParseException ex, HttpStatusCode status, WebRequest request) {

    String error = null;
    try {
      error =
          "There was an error near token '"
              + ex.getProcessor().getCurrentName()
              + "' please check it.";
    } catch (IOException e) {
      log.error("IOException", e);
    }

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.JSON_SINTAXIS_NOT_VALID)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.JSON_SINTAXIS_NOT_VALID.getHttpStatus(),
        request);
  }

  @ExceptionHandler({JsonMappingException.class})
  public ResponseEntity<Object> handleJsonMappingException(
      JsonMappingException ex, HttpStatusCode status, WebRequest request) {

    String error =
        "There was an error near token '"
            + ex.getPathReference().split("\"")[1]
            + "' please check it.";

    return buildResponseEntity(
        ErrorResponse.builder()
            .reason(ErrorReason.JSON_MAPPING_NOT_VALID)
            .errors(Collections.singletonList(error))
            .build(),
        ErrorReason.JSON_MAPPING_NOT_VALID.getHttpStatus(),
        request);
  }

  @ExceptionHandler({ApiException.class})
  public ResponseEntity<Object> handleApiRestException(ApiException ex, WebRequest request) {
    return buildResponseEntity(
        ErrorResponse.builder().reason(ex.getReason()).message(ex.getMessage()).build(),
        ex.getReason().getHttpStatus(),
        request);
  }

  private List<String> getErrors(List<FieldError> fieldErrors, List<ObjectError> objectErrors) {
    List<String> errors = new ArrayList<>();
    fieldErrors.forEach(
        fieldError -> errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
    objectErrors.forEach(
        objectError ->
            errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage()));
    return errors;
  }

  private ResponseEntity<Object> buildResponseEntity(
      ErrorResponse errorResponse, HttpStatus status, WebRequest request) {
    log.error(
        "Failed {} request {}",
        ((ServletWebRequest) request).getHttpMethod(),
        request.getDescription(false));
    log.error(
        "Request parameters isEmpty={} : {}",
        request.getParameterMap().isEmpty(),
        request.getParameterMap());
    log.error("Http status error : {}", status);
    log.error("ErrorResponse : {}", errorResponse);
    return new ResponseEntity<>(errorResponse, status);
  }
}
