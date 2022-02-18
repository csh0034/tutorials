package com.ask.springwebflux.advice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(Exception.class)
  public Mono<Map<String, String>> handleException(Exception e) {
    Map<String, String> response = new LinkedHashMap<>();
    response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    response.put("code", "40999");
    response.put("message", e.getMessage());
    return Mono.just(response);
  }

}
