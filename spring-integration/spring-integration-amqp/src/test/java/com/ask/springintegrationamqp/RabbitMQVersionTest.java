package com.ask.springintegrationamqp;

import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.domain.OverviewResponse;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
@Slf4j
class RabbitMQVersionTest {

  @Autowired
  private Client client;

  @Test
  void version() {
    OverviewResponse overview = client.getOverview();
    String serverVersion = overview.getServerVersion();
    log.info("serverVersion: {}", serverVersion);
  }

  @TestConfiguration
  @RequiredArgsConstructor
  public static class TestConfig {

    private final RabbitProperties rabbitProperties;

    @Bean
    public Client client() throws MalformedURLException, URISyntaxException {
      return new Client("http://localhost:15672/api/", rabbitProperties.getUsername(), rabbitProperties.getPassword());
    }

  }

}

