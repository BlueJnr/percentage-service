package com.tenpo.percentage.config.filter;

import com.tenpo.percentage.service.HistoryService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestLoggingFilter implements Filter {
  private final HistoryService historyService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

    chain.doFilter(wrappedRequest, wrappedResponse);

    String requestBody = getRequestBody(wrappedRequest);
    String responseBody = getResponseBody(wrappedResponse);

    historyService.logCall(
        wrappedRequest.getRequestURI(), requestBody, responseBody, wrappedResponse.getStatus());

    wrappedResponse.copyBodyToResponse();
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    byte[] content = request.getContentAsByteArray();
    return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "{}";
  }

  private String getResponseBody(ContentCachingResponseWrapper response) {
    byte[] content = response.getContentAsByteArray();
    return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "{}";
  }
}
