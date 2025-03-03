package com.tenpo.percentage.config.exception;

import com.tenpo.percentage.config.errors.enums.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiException extends RuntimeException {
  private final ErrorReason reason;
  private final String message;
}
