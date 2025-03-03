package com.tenpo.percentage.config;

import com.tenpo.percentage.config.filter.RequestLoggingFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

  private final RequestLoggingFilter requestLoggingFilter;

  @Bean
  public FilterRegistrationBean<Filter> loggingFilter() {
    FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(requestLoggingFilter);
    registrationBean.addUrlPatterns("/api/calculate", "/api/calculate/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}
