package com.ask.stream.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StreamConfig {

  @Bean
  public Supplier<String> outSample1() {
    // null 리턴할 경우 안보내짐
    return () -> "produce outSample1";
  }

  @Bean
  public Supplier<Map<String, String>> outSample2() {
    return () -> {
      Map<String, String> map = new LinkedHashMap<>();
      map.put("title", "ASk");
      map.put("content", "produce outSample2");
      return map;
    };
  }

  @Bean
  public ApplicationRunner applicationRunner(StreamBridge streamBridge) {
    return args -> {
      log.info("invoke applicationRunner");
      streamBridge.send("outSample1-out-0", "applicationRunner...");
    };
  }
}
