package com.tenpo.percentage.expose.web;

import com.tenpo.percentage.api.HistoryApiDelegate;
import com.tenpo.percentage.model.CallHistory;
import com.tenpo.percentage.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryApiImpl implements HistoryApiDelegate {
  private final HistoryService historyService;

  @Override
  public ResponseEntity<List<CallHistory>> historyGet(Integer page, Integer size) {
    return ResponseEntity.ok(historyService.getCallHistory(page, size));
  }
}
