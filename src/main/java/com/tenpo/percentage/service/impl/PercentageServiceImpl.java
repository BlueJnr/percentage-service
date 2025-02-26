package com.tenpo.percentage.service.impl;

import com.tenpo.percentage.client.api.DefaultApi;
import com.tenpo.percentage.config.exception.CacheValueNotFoundException;
import com.tenpo.percentage.service.CacheService;
import com.tenpo.percentage.service.PercentageService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class PercentageServiceImpl implements PercentageService {

  private final DefaultApi defaultApi;
  private final CacheService cacheService;

  @Override
  public BigDecimal getValue() {
    try {
      BigDecimal value = defaultApi.externalPercentageGet().getValue();
      cacheService.savePercentage(value);
      return value;
    } catch (RestClientException exception) {
      return cacheService
          .getPercentage()
          .orElseThrow(
              () ->
                  new CacheValueNotFoundException(
                      "No percentage available in cache and external service failed"));
    }
  }
}
