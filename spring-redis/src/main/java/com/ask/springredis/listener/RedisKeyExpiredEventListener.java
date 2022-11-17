package com.ask.springredis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpiredEventListener {

  @EventListener(RedisKeyExpiredEvent.class)
  public void handleEvent(RedisKeyExpiredEvent<?> event) {
    log.info("event : {}", event);
  }

}
