package com.tenpo.percentage.service;

import com.tenpo.percentage.model.CalculationRequest;
import com.tenpo.percentage.model.CalculationResponse;

public interface CalculateService {

  CalculationResponse apply(CalculationRequest calculationRequest);
}
