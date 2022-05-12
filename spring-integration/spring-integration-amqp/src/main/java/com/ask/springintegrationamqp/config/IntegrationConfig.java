package com.ask.springintegrationamqp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class IntegrationConfig {

  private final SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

  @Bean
  public IntegrationFlow sampleFlow() {
    SimpleMessageListenerContainer listenerContainer = rabbitListenerContainerFactory.createListenerContainer();
    listenerContainer.setQueueNames(QueueConstants.TEST_QUEUE_NAME);

    return IntegrationFlows.from(Amqp.inboundAdapter(listenerContainer))
        .transform(Transformers.objectToString())
        .handle(message -> log.info("message: {}", message))
        .get();
  }

}
