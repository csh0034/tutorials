package com.ask.apachecamel;

import java.util.concurrent.TimeUnit;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class TimerTest {

  @Test
  void timer() throws InterruptedException {
    TimeUnit.SECONDS.sleep(5);
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    public EndpointRouteBuilder timerRoute() {
      return new EndpointRouteBuilder() {
        @Override
        public void configure() {
          from("timer:foo?fixedRate=true&period=1000")
              .process(exchange -> log.info("invoke timer"));
        }
      };
    }

  }

}
