package com.ask.springjpacore.web;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class CompanyControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Value("${local.server.port}")
  private int port;

  @DisplayName("osiv 가 꺼져있을 경우 컨트롤러에서 조회후 merge 호출시 id가 있기 때문에 다시 조회함")
  @Test
  void transaction() {
    String addUrl = String.format("http://localhost:%d/add", port);
    String companyId = restTemplate.postForObject(addUrl, null, String.class);
    log.info("companyId: {}", companyId);

    String updateUrl = String.format("http://localhost:%d/update/%s", port, companyId);
    String result = restTemplate.postForObject(updateUrl, null, String.class);
    assertThat(result).isEqualTo("success");
  }

}
