package com.tenpo.percentage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "call_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime timestamp;

  private String endpoint;

  @Lob private String requestParams;

  @Lob private String responseData;

  private Integer status;
}
