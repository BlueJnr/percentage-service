package com.tenpo.percentage.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface CacheService {
  void savePercentage(BigDecimal percentage);

  Optional<BigDecimal> getPercentage();
}
