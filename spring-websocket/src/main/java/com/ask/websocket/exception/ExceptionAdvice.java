package com.ask.websocket.exception;

import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionAdvice {

  @MessageExceptionHandler
  public Map<String, String> handleException(Exception e) {
    return Map.of("error", e.getMessage());
  }

}
