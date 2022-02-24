package com.ask.springtestcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

  @Bean
  public String sampleTitle() {
    return "sampleTitle";
  }
}
