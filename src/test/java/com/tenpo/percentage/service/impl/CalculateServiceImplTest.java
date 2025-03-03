package com.tenpo.percentage.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenpo.percentage.model.CalculationRequest;
import com.tenpo.percentage.model.CalculationResponse;
import com.tenpo.percentage.service.PercentageService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculateServiceImplTest {
  @Mock private PercentageService percentageService;

  @InjectMocks private CalculateServiceImpl calculateService;

  @Test
  void shouldApplyPercentageCorrectly() {
    // Arrange
    CalculationRequest request = new CalculationRequest();
    request.setNum1(BigDecimal.valueOf(100));
    request.setNum2(BigDecimal.valueOf(50));

    when(percentageService.getValue()).thenReturn(BigDecimal.valueOf(10));

    // Act
    CalculationResponse response = calculateService.apply(request);

    // Assert
    assertNotNull(response);
    assertEquals(
        BigDecimal.valueOf(10), response.getPercentage());
    assertEquals(BigDecimal.valueOf(165.00), response.getResult());
  }

  @Test
  void shouldHandleZeroPercentage() {
    // Arrange
    CalculationRequest request = new CalculationRequest();
    request.setNum1(BigDecimal.valueOf(200));
    request.setNum2(BigDecimal.valueOf(100));

    when(percentageService.getValue()).thenReturn(BigDecimal.ZERO);

    // Act
    CalculationResponse response = calculateService.apply(request);

    // Assert
    assertNotNull(response);
    assertEquals(BigDecimal.ZERO, response.getPercentage());
    assertEquals(BigDecimal.valueOf(300), response.getResult());
  }
}
