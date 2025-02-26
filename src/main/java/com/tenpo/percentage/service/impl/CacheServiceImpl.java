package com.tenpo.percentage.service.impl;

import com.tenpo.percentage.service.CacheService;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

  private final RedisTemplate<String, String> redisTemplate;
  private static final String CACHE_KEY = "percentage";

  @Override
  public void savePercentage(BigDecimal percentage) {
    redisTemplate.opsForValue().set(CACHE_KEY, percentage.toString(), Duration.ofMinutes(30));
  }

  @Override
  public Optional<BigDecimal> getPercentage() {
    return Optional.ofNullable(redisTemplate.opsForValue().get(CACHE_KEY)).map(BigDecimal::new);
  }
}
