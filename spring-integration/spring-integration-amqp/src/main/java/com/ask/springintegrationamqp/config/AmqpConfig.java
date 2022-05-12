package com.ask.springintegrationamqp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @see org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
 */
@Configuration
public class AmqpConfig {

  @Bean(name = QueueConstants.TEST_QUEUE_NAME)
  public Queue testQueue() {
    return QueueBuilder.nonDurable(QueueConstants.TEST_QUEUE_NAME)
        .exclusive()
        .build();
  }

  @Bean
  public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer() {
    return container -> {
      container.setDefaultRequeueRejected(false);
      container.setConcurrentConsumers(2);
    };
  }

  /**
   * json 으로 메세지 전송시 필요함, 수신만 할 경우 필요 없음
   */
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
