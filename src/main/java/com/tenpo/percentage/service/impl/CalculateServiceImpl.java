package com.tenpo.percentage.service.impl;

import com.tenpo.percentage.model.CalculationRequest;
import com.tenpo.percentage.model.CalculationResponse;
import com.tenpo.percentage.service.CalculateService;
import com.tenpo.percentage.service.PercentageService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {

  private final PercentageService percentageService;

  @Override
  public CalculationResponse apply(CalculationRequest calculationRequest) {
    BigDecimal percentage = percentageService.getValue();
    BigDecimal sum = calculationRequest.getNum1().add(calculationRequest.getNum2());
    BigDecimal result = sum.add(sum.multiply(percentage.divide(BigDecimal.valueOf(100))));

    CalculationResponse calculationResponse = new CalculationResponse();
    calculationResponse.setPercentage(percentage);
    calculationResponse.setResult(result);
    return calculationResponse;
  }
}
