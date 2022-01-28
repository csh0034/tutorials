package com.ask.stream.config;

import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StreamConfig {

  @Bean
  public Consumer<String> inSample1() {
    return message -> log.info("consume message : {}", message);
  }

  @Bean
  public Consumer<Map<String, String>> inSample2() {
    return map -> log.info("consume message : {}", map);
  }
}
