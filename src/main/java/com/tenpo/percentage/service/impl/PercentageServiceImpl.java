package com.tenpo.percentage.service.impl;

import com.tenpo.percentage.client.api.DefaultApi;
import com.tenpo.percentage.config.exception.CacheValueNotFoundException;
import com.tenpo.percentage.service.CacheService;
import com.tenpo.percentage.service.PercentageService;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PercentageServiceImpl implements PercentageService {

  private final DefaultApi defaultApi;
  private final CacheService cacheService;

  @Override
  @Retry(name = "percentageServiceRetry", fallbackMethod = "getCachedPercentage")
  public BigDecimal getValue() {
    log.info("Calling external service...");
    BigDecimal value = defaultApi.externalPercentageGet().getValue();
    cacheService.savePercentage(value);
    return value;
  }

  public BigDecimal getCachedPercentage(Exception e) {
    return cacheService
        .getPercentage()
        .orElseThrow(
            () ->
                new CacheValueNotFoundException(
                    "No percentage available in cache and external service failed"));
  }
}
