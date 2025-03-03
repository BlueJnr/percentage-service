package com.tenpo.percentage.respository;

import static org.assertj.core.api.Assertions.assertThat;

import com.tenpo.percentage.model.CallHistoryEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.utility.TestcontainersConfiguration;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ImportTestcontainers(TestcontainersConfiguration.class)
class CallHistoryRepositoryTest {

  @Autowired private CallHistoryRepository callHistoryRepository;

  @Test
  void shouldSaveAndRetrieveCallHistory() {
    // Arrange
    CallHistoryEntity entity =
        CallHistoryEntity.builder()
            .timestamp(LocalDateTime.now())
            .endpoint("/api/calculate")
            .requestParams("{ \"num1\": \"5.00\", \"num2\": \"10.00\" }")
            .responseData("{ \"result\": \"16.50\", \"percentage\": \"10.00\" }")
            .status(200)
            .build();

    // Act
    callHistoryRepository.save(entity);
    List<CallHistoryEntity> historyList = callHistoryRepository.findAll();

    // Assert
    assertThat(historyList).isNotEmpty();
    assertThat(historyList.getFirst().getEndpoint()).isEqualTo("/api/calculate");
  }
}
