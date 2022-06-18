package com.ask.awsmessagingsqs.config;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

@Configuration
@Slf4j
public class ListenerConfig {

  @SqsListener(SqsConstants.STANDARD_QUEUE)
  void standardQueueListener(String message) {
    log.info("standard queue message received: {}", message);
  }

  @SqsListener(SqsConstants.FIFO_QUEUE)
  void fifoQueueListener(String message) {
    log.info("fifo queue message received: {}", message);
  }

  @MessageExceptionHandler(Exception.class)
  void handleException(Exception e) {
    log.info("exception...", e);
  }

}
