package com.ask.springredis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;

@Slf4j
@RequiredArgsConstructor
public abstract class RedisTopicSubscriberAdaptor<T> implements MessageListener {

  private final ObjectMapper objectMapper;
  private final Class<T> clazz;

  @Override
  public void onMessage(Message message, @Nullable byte[] pattern) {
    try {
      handle(objectMapper.readValue(message.getBody(), clazz));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void handle(T message);

}
