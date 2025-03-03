package com.tenpo.percentage.config.errors.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tenpo.percentage.config.errors.enums.ErrorReason;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {
  private ErrorReason reason;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<String> errors;
}
