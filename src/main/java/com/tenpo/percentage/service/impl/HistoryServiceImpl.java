package com.tenpo.percentage.service.impl;

import com.tenpo.percentage.model.CallHistory;
import com.tenpo.percentage.model.CallHistoryEntity;
import com.tenpo.percentage.respository.CallHistoryRepository;
import com.tenpo.percentage.service.HistoryService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
  private final CallHistoryRepository callHistoryRepository;

  @Override
  public void logCall(String endpoint, String requestParams, String responseData, int status) {
    CallHistoryEntity callHistoryEntity =
        CallHistoryEntity.builder()
            .timestamp(LocalDateTime.now())
            .endpoint(endpoint)
            .requestParams(requestParams)
            .responseData(responseData)
            .status(status)
            .build();
    callHistoryRepository.saveAndFlush(callHistoryEntity);
  }

  @Override
  public List<CallHistory> getCallHistory(Integer page, Integer size) {
    return callHistoryRepository
        .findAll(PageRequest.of(page, size))
        .map(
            callHistoryEntity -> {
              CallHistory callHistory = new CallHistory();
              callHistory.setTimestamp(
                  callHistoryEntity.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
              callHistory.setEndpoint(callHistoryEntity.getEndpoint());
              callHistory.setStatus(callHistoryEntity.getStatus());
              callHistory.setRequestParams(callHistoryEntity.getRequestParams());
              callHistory.setResponseData(callHistoryEntity.getResponseData());
              callHistory.status(callHistoryEntity.getStatus());
              return callHistory;
            })
        .toList();
  }
}
