package com.ask.springintegrationamqp.config;

import com.ask.springintegrationamqp.handler.SampleHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class IntegrationConfig {

  private final SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;
  private final SampleHandler sampleHandler;

  @Bean
  public DirectChannel bridgeChannel() {
    return MessageChannels.direct().get();
  }

  @Bean
  public IntegrationFlow sampleFlow() {
    SimpleMessageListenerContainer listenerContainer = rabbitListenerContainerFactory.createListenerContainer();
    listenerContainer.setQueueNames(QueueConstants.TEST_QUEUE_NAME);

    return IntegrationFlows.from(Amqp.inboundAdapter(listenerContainer).outputChannel(bridgeChannel()).id("sampleFlowAdaptor"))
        .transform(Transformers.objectToString(), endpointSpec -> endpointSpec.id("sampleFlowTransformerConsumer"))
        .handle(sampleHandler, "handle")
        .get();
  }

}
