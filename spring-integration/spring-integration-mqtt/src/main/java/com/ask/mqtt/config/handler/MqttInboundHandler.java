package com.ask.mqtt.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttInboundHandler {

  public void handle(String message) {
    log.info("message: {}", message);
  }

}
