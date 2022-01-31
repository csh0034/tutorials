package com.ask.function;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@FunctionalSpringBootTest
@AutoConfigureWebTestClient
@Slf4j
class SpringCloudFunctionApplicationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void uppercase() {
    webTestClient.post()
        .uri("/uppercase")
        .body(Mono.just("hello"), String.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("HELLO");
  }

  @Test
  void timestamp() {
    String result = webTestClient.post()
        .uri("/timestamp")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).returnResult()
        .getResponseBody();

    log.info("result : {}", result);
    assertThat(result).isNotNull();
  }
}
