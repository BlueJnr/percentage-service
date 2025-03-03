package com.tenpo.percentage.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenpo.percentage.config.errors.enums.ErrorReason;
import com.tenpo.percentage.config.exception.ApiException;
import com.tenpo.percentage.model.CallHistory;
import com.tenpo.percentage.model.CallHistoryEntity;
import com.tenpo.percentage.respository.CallHistoryRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {

  @Mock private CallHistoryRepository callHistoryRepository;

  @InjectMocks private HistoryServiceImpl historyService;

  @Test
  void shouldLogCallSuccessfully() {
    // Arrange
    CallHistoryEntity entity =
        CallHistoryEntity.builder()
            .timestamp(LocalDateTime.now())
            .endpoint("/api/test")
            .requestParams("{ \"param\": \"value\" }")
            .responseData("{ \"result\": \"success\" }")
            .status(200)
            .build();

    when(callHistoryRepository.saveAndFlush(any(CallHistoryEntity.class))).thenReturn(entity);

    // Act
    historyService.logCall(
        "/api/test", "{ \"param\": \"value\" }", "{ \"result\": \"success\" }", 200);

    // Assert
    verify(callHistoryRepository, times(1)).saveAndFlush(any(CallHistoryEntity.class));
  }

  @Test
  void shouldRetrieveCallHistorySuccessfully() {
    // Arrange
    CallHistoryEntity entity =
        CallHistoryEntity.builder()
            .timestamp(LocalDateTime.now())
            .endpoint("/api/test")
            .requestParams("{ \"param\": \"value\" }")
            .responseData("{ \"result\": \"success\" }")
            .status(200)
            .build();

    Page<CallHistoryEntity> page = new PageImpl<>(Collections.singletonList(entity));
    when(callHistoryRepository.findAll(any(PageRequest.class))).thenReturn(page);

    // Act
    List<CallHistory> result = historyService.getCallHistory(0, 10);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("/api/test", result.getFirst().getEndpoint());
  }

  @Test
  void shouldThrowExceptionWhenRateLimitExceeded() {
    // Simulamos que el RateLimiter bloquea la solicitud
    Exception rateLimitException = new RuntimeException("Rate limit exceeded");

    // Act & Assert
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              historyService.historyServiceRateLimitFallback(0, 10, rateLimitException);
            });

    assertEquals("Rate limit exceeded. Try again later.", exception.getMessage());
    assertEquals(ErrorReason.TOO_MANY_REQUESTS, exception.getReason());
  }

  @Test
  void shouldHandleServiceFailureGracefully() {
    // Simular fallo del repositorio (servicio externo fallando)
    when(callHistoryRepository.findAll(any(PageRequest.class)))
        .thenThrow(new RuntimeException("Database failure"));

    // Act & Assert
    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              historyService.getCallHistory(0, 10);
            });

    assertEquals("Database failure", exception.getMessage());
  }
}
