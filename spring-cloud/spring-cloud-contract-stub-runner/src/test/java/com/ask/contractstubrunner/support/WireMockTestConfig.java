package com.ask.contractstubrunner.support;

import static com.ask.contractstubrunner.support.WireMockTest.MOCK_URI_PLACEHOLDER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class WireMockTestConfig {

  @Value(MOCK_URI_PLACEHOLDER)
  private String mockUri;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.rootUri(mockUri)
        .build();
  }

}
