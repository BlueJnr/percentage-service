package com.tenpo.percentage.expose.web;

import com.tenpo.percentage.api.CalculateApiDelegate;
import com.tenpo.percentage.model.CalculationRequest;
import com.tenpo.percentage.model.CalculationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CalculateApiImpl implements CalculateApiDelegate {

  @Override
  public ResponseEntity<CalculationResponse> calculatePost(CalculationRequest calculationRequest) {
    return CalculateApiDelegate.super.calculatePost(calculationRequest);
  }
}
