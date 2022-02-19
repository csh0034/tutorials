package com.ask.openfeign.client;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;

@SpringBootTest
@Slf4j
class GoogleClientTest {

  @Autowired
  private GoogleClient googleClient;

  @Test
  void index() {
    String index = googleClient.index();
    log.info("index : {}", index);
  }

  @DisplayName("circuit breaker 적용후에 Fallback 지정안할 경우 예외 검증")
  @Test
  void search() {
    assertThatExceptionOfType(NoFallbackAvailableException.class)
        .isThrownBy(() -> googleClient.search("google"));
  }

}
