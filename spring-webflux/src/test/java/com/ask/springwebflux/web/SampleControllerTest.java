package com.ask.springwebflux.web;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Slf4j
class SampleControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void sample() {
    String result = webTestClient.get()
        .uri("/mono")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class).returnResult()
        .getResponseBody();

    assertThat(result).isEqualTo("Hello");
  }
}
