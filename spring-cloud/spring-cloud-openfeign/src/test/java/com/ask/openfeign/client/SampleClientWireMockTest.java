package com.ask.openfeign.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@DisplayName("wire-mock 을 통한 feign client 검증")
@SpringBootTest("feign.custom.sample-url=http://localhost:${wiremock.server.port}")
@AutoConfigureWireMock(port = 0)
@Slf4j
class SampleClientWireMockTest {

  @Autowired
  private SampleClient sampleClient;

  @Test
  void index() {
    // when
    String result = sampleClient.index();

    // then
    assertThat(result).isEqualTo("success");
  }

  @Test
  void time() {
    // when
    LocalDateTime now = LocalDateTime.now();
    String result = sampleClient.time(now);

    // then
    assertThat(result).isEqualTo("now-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }

}
