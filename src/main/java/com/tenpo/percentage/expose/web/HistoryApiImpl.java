package com.tenpo.percentage.expose.web;

import com.tenpo.percentage.api.HistoryApiDelegate;
import com.tenpo.percentage.model.CallHistory;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HistoryApiImpl implements HistoryApiDelegate {

  @Override
  public ResponseEntity<List<CallHistory>> historyGet(Integer page, Integer size) {
    return HistoryApiDelegate.super.historyGet(page, size);
  }
}
