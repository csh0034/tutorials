package com.ask.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 9999)
@TestPropertySource(locations = "classpath:jwt.properties", properties = "spring.sleuth.enabled=false")
class JwtAuthenticationGatewayFilterTest {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private Environment env;

  @DisplayName("JwtAuthentication Filter 인증 통과")
  @Test
  void jwtFilterOk() {
    // given
    String validToken = env.getProperty("jwt.valid");

    // when then
    webTestClient.get()
        .uri("/auth")
        .header(HttpHeaders.AUTHORIZATION, validToken)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.X-Authorization-Id").isEqualTo("ASk")
        .jsonPath("$.X-Authorization-Role").isEqualTo("ROLE_ADMIN");
  }

  @DisplayName("JwtAuthentication Filter 실패, 토큰없음")
  @Test
  void jwtFilterNoToken() {
    // when then
    webTestClient.get()
        .uri("/auth")
        .exchange()
        .expectStatus().isBadRequest()
        .expectHeader().contentType(MediaType.TEXT_PLAIN_VALUE)
        .expectBody(String.class)
        .isEqualTo("missing authorization header");
  }

  @DisplayName("JwtAuthentication Filter 실패, invalid 토큰")
  @Test
  void jwtFilterInvalidToken() {
    // given
    String invalidToken = "invalid-token";

    // when then
    webTestClient.get()
        .uri("/auth")
        .header(HttpHeaders.AUTHORIZATION, invalidToken)
        .exchange()
        .expectStatus().isBadRequest()
        .expectHeader().contentType(MediaType.TEXT_PLAIN_VALUE)
        .expectBody(String.class)
        .isEqualTo("invalid authorization header");
  }

  @DisplayName("JwtAuthentication Filter 실패, 권한없음")
  @Test
  void jwtFilterInvalidRole() {
    // given
    String invalidRoleToken = env.getProperty("jwt.invalid.role");

    // when then
    webTestClient.get()
        .uri("/auth")
        .header(HttpHeaders.AUTHORIZATION, invalidRoleToken)
        .exchange()
        .expectStatus().isForbidden()
        .expectHeader().contentType(MediaType.TEXT_PLAIN_VALUE)
        .expectBody(String.class)
        .isEqualTo("invalid role");
  }

}
