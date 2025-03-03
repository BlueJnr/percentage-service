package com.tenpo.percentage.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {
  @Mock private RedisTemplate<String, String> redisTemplate;

  @Mock private ValueOperations<String, String> valueOperations;

  @InjectMocks private CacheServiceImpl cacheService;

  private static final String CACHE_KEY = "percentage";

  @BeforeEach
  void setUp() {
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  void shouldSavePercentageInCache() {
    // Arrange
    BigDecimal percentage = BigDecimal.valueOf(12.5);

    // Act
    cacheService.savePercentage(percentage);

    // Assert
    verify(valueOperations).set(CACHE_KEY, "12.5", Duration.ofMinutes(30));
  }

  @Test
  void shouldReturnPercentageFromCache() {
    // Arrange
    when(valueOperations.get(CACHE_KEY)).thenReturn("15.0");

    // Act
    Optional<BigDecimal> result = cacheService.getPercentage();

    // Assert
    assertTrue(result.isPresent());
    assertEquals(BigDecimal.valueOf(15.0), result.get());
  }

  @Test
  void shouldReturnEmptyWhenCacheIsEmpty() {
    // Arrange
    when(valueOperations.get(CACHE_KEY)).thenReturn(null);

    // Act
    Optional<BigDecimal> result = cacheService.getPercentage();

    // Assert
    assertFalse(result.isPresent());
  }
}
