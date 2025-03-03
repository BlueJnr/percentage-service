package com.tenpo.percentage.service;

import com.tenpo.percentage.model.CallHistory;
import java.util.List;

public interface HistoryService {
  void logCall(String endpoint, String requestParams, String responseData, int status);

  List<CallHistory> getCallHistory(Integer page, Integer size);
}
