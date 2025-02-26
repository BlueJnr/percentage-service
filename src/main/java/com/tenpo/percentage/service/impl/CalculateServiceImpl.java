package com.tenpo.percentage.service.impl;

import com.tenpo.percentage.client.api.DefaultApi;
import com.tenpo.percentage.client.model.ExternalPercentageGet200Response;
import com.tenpo.percentage.model.CalculationRequest;
import com.tenpo.percentage.model.CalculationResponse;
import com.tenpo.percentage.service.CalculateService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {

  private final DefaultApi defaultApi;

  @Override
  public CalculationResponse apply(CalculationRequest calculationRequest) {
    ExternalPercentageGet200Response percentageResponse = defaultApi.externalPercentageGet();
    BigDecimal sum = calculationRequest.getNum1().add(calculationRequest.getNum2());
    BigDecimal result =
        percentageResponse
            .getPercentage()
            .add(BigDecimal.valueOf(100.00))
            .multiply(sum)
            .divide(BigDecimal.valueOf(100.00));

    CalculationResponse calculationResponse = new CalculationResponse();
    calculationResponse.setPercentage(percentageResponse.getPercentage());
    calculationResponse.setResult(result);
    return calculationResponse;
  }
}
