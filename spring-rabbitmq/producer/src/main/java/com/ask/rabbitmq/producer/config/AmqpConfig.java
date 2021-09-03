package com.ask.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

  public static final String EXCHANGE_NAME = "sample.exchange";
  public static final String QUEUE_NAME_1 = "sample.queue1";
  public static final String QUEUE_NAME_2 = "sample.queue2";
  public static final String QUEUE_NAME_3 = "sample.queue3";
  public static final String ROUTING_KEY = "sample.routing.key.#";

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Queue queue1() {
    return new Queue(QUEUE_NAME_1);
  }

  @Bean
  public Queue queue2() {
    return new Queue(QUEUE_NAME_2);
  }

  @Bean
  public Queue queue3() {
    return new Queue(QUEUE_NAME_3);
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(queue1()).to(exchange()).with(ROUTING_KEY);
  }

  // default SimpleMessageConverter
  // 아래 빈 설정시 교체되며 Json 처리
  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
