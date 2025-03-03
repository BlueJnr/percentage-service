package com.tenpo.percentage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PercentageApplication {

  public static void main(String[] args) {
    SpringApplication.run(PercentageApplication.class, args);
  }
}
