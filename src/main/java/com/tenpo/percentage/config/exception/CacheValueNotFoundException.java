package com.tenpo.percentage.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // 503
public class CacheValueNotFoundException extends RuntimeException {
  public CacheValueNotFoundException(String message) {
    super(message);
  }
}
