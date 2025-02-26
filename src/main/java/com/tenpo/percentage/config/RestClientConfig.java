package com.tenpo.percentage.config;

import com.tenpo.percentage.client.api.DefaultApi;
import com.tenpo.percentage.client.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

  @Bean
  public DefaultApi defaultApi(ApiClient apiClient) {
    return new DefaultApi(apiClient);
  }

  @Bean
  public ApiClient apiClient() {
    return new ApiClient();
  }
}
