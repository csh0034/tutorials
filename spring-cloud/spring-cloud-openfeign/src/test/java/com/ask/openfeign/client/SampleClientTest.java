package com.ask.openfeign.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SampleClientTest {

  @Autowired
  private SampleClient sampleClient;

  @Test
  void index() {
    // when
    String result = sampleClient.index();

    // then
    assertThat(result).isEqualTo(SampleClientFallback.INDEX_NO_FALLBACK_MESSAGE);
  }

  @Test
  void time() {
    // when
    String result = sampleClient.time(LocalDateTime.now());

    // then
    assertThat(result).isEqualTo(SampleClientFallback.TIME_NO_FALLBACK_MESSAGE);
  }

}
