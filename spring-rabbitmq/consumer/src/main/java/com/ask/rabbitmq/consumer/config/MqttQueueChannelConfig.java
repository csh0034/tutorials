package com.ask.rabbitmq.consumer.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class MqttQueueChannelConfig {

  //@Bean
  public PollerMetadata poller() {
    return Pollers.fixedRate(5, TimeUnit.SECONDS).get();
  }

  // default poller 지정
  //@Bean(name = PollerMetadata.DEFAULT_POLLER)
  public PollerMetadata defaultPoller() {
    return Pollers.fixedRate(500).get();
  }
}
