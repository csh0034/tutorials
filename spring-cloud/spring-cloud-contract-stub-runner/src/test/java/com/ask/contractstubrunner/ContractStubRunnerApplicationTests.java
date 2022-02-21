package com.ask.contractstubrunner;

import static com.ask.contractstubrunner.support.WireMockTest.MOCK_URI_PLACEHOLDER;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.ask.contractstubrunner.support.WireMockTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@WireMockTest
@Slf4j
class ContractStubRunnerApplicationTests {

  @Autowired
  private WireMockServer wireMockServer;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Value(MOCK_URI_PLACEHOLDER)
  private String mockUri;

  @DisplayName("mappings 하위 json 파일을 통한 mock 처리 검증1")
  @Test
  void jsonStub1() {
    // when
    String result = restTemplate.getForObject("/v1/check?username=test", String.class);

    // then
    log.info("result : {}", result);
    assertThat(result).isEqualTo("true");
  }

  @DisplayName("mappings 하위 json 파일을 통한 mock 처리 검증2")
  @Test
  void jsonStub2() {
    // when
    String result = restTemplate.getForObject("/v1/headers", String.class);

    // then
    log.info("result : {}", result);
    assertThat(result).contains("request.host", "request.port", "request.headers.user-agent");
  }

  @DisplayName("java 설정을 통한 mock 처리 검증1")
  @Test
  void javaStub1() {
    // given
    wireMockServer.stubFor(get("/v1/stub1")
        .willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .withBody("stub1")));

    // when
    String result = restTemplate.getForObject("/v1/stub1", String.class);

    // then
    wireMockServer.verify(getRequestedFor(urlEqualTo("/v1/stub1")));

    log.info("result : {}", result);
    assertThat(result).isEqualTo("stub1");
  }

  @DisplayName("java 설정을 통한 mock 처리 검증2")
  @Test
  void javaStub2() throws Exception {
    // given
    Map<String, String> response = new LinkedHashMap<>();
    response.put("request.host", "{{request.host}}");
    response.put("request.port", "{{request.port}}");
    response.put("request.headers.user-agent", "{{request.headers.user-agent}}");

    wireMockServer.stubFor(get("/v1/stub2")
        .willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBody(objectMapper.writeValueAsString(response))
            .withTransformers(ResponseTemplateTransformer.NAME)));

    // when
    String result = restTemplate.getForObject("/v1/stub2", String.class);

    // then
    wireMockServer.verify(getRequestedFor(urlEqualTo("/v1/stub2")));

    log.info("result : {}", result);
    assertThat(result).contains("request.host", "request.port", "request.headers.user-agent");
  }

}
