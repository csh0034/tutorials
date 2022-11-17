package com.ask.springredis.listener;

import com.ask.springredis.config.RedisTopicSubscriberAdaptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisTopicSubscriber extends RedisTopicSubscriberAdaptor<UserMessage> {

  public RedisTopicSubscriber(ObjectMapper objectMapper) {
    super(objectMapper, UserMessage.class);
  }

  @Override
  protected void handle(UserMessage message) {
    log.info("Message received: {}", message);
  }

}
