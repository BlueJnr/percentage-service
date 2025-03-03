package com.tenpo.percentage.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenpo.percentage.client.api.DefaultApi;
import com.tenpo.percentage.client.model.ExternalPercentageGet200Response;
import com.tenpo.percentage.config.errors.enums.ErrorReason;
import com.tenpo.percentage.config.exception.ApiException;
import com.tenpo.percentage.service.CacheService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PercentageServiceImplTest {

  @Mock private DefaultApi defaultApi;
  @Mock private CacheService cacheService;
  @InjectMocks private PercentageServiceImpl percentageService;

  @Test
  void shouldReturnPercentageFromExternalService() {
    // Arrange
    ExternalPercentageGet200Response response = new ExternalPercentageGet200Response();
    response.setValue(BigDecimal.valueOf(10.0));

    when(defaultApi.externalPercentageGet()).thenReturn(response);

    // Act
    BigDecimal result = percentageService.getValue();

    // Assert
    assertNotNull(result);
    assertEquals(BigDecimal.valueOf(10.0), result);
    verify(cacheService).savePercentage(BigDecimal.valueOf(10.0));
  }

  @Test
  void shouldReturnCachedPercentageWhenExternalServiceFails() {
    // Arrange
    when(cacheService.getPercentage()).thenReturn(Optional.of(BigDecimal.valueOf(8.5)));

    // Act
    BigDecimal result = percentageService.getCachedPercentage(new RuntimeException());

    // Assert
    assertNotNull(result);
    assertEquals(BigDecimal.valueOf(8.5), result);
  }

  @Test
  void shouldThrowExceptionWhenNoCachedPercentageAvailable() {
    // Arrange
    when(cacheService.getPercentage()).thenReturn(Optional.empty());

    // Act & Assert
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              percentageService.getCachedPercentage(new RuntimeException());
            });

    assertEquals(
        "No percentage available in cache and external service failed", exception.getMessage());
    assertEquals(ErrorReason.SERVICE_UNAVAILABLE, exception.getReason());
  }
}
