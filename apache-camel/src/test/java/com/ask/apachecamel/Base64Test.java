package com.ask.apachecamel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
@Slf4j
class Base64Test {

  @Autowired
  private ProducerTemplate producerTemplate;

  @Test
  void base64() {
    producerTemplate.sendBody("direct:base64", "1234ABCd!@#$%^");
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    public EndpointRouteBuilder base64Route() {
      return new EndpointRouteBuilder() {
        @Override
        public void configure() {
          from("direct:base64")
              .to("dataformat:base64:marshal")
              .log("${body}")
              .to("dataformat:base64:unmarshal")
              .log("${body}");
        }
      };
    }

  }

}
