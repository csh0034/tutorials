package com.ask.integration.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisTopicHandler {

  public void handleDefault(RedisTopicMessage message) {
    log.info("handleDefault: {}", message);
  }

  public void handleUser(RedisTopicMessage message) {
    log.info("handleUser: {}", message);
  }

  @Getter
  @ToString
  public static class RedisTopicMessage {

    private String id;
    private String name;

  }

}
