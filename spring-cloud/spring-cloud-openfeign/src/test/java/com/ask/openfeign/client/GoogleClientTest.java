package com.ask.openfeign.client;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

  @Test
  void search() {
    assertThatExceptionOfType(FeignException.class)
        .isThrownBy(() -> googleClient.search("google"))
        .withMessageContaining("403 Forbidden");
  }

}
