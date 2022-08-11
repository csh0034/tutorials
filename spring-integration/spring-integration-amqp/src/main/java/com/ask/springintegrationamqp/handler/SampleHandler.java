package com.ask.springintegrationamqp.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleHandler {

  public void handle(Message<String> message) {
    log.info("message: {}", message);
  }

}
