package com.tenpo.percentage.expose.web;

import com.tenpo.percentage.api.CalculateApiDelegate;
import com.tenpo.percentage.model.CalculationRequest;
import com.tenpo.percentage.model.CalculationResponse;
import com.tenpo.percentage.service.CalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CalculateApiImpl implements CalculateApiDelegate {

  private final CalculateService calculateService;

  @Override
  public ResponseEntity<CalculationResponse> calculatePost(CalculationRequest calculationRequest) {
    return ResponseEntity.ok(calculateService.apply(calculationRequest));
  }
}
