package com.ask.stream.config;

import java.util.Map;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
@Slf4j
public class StreamConfig {

  @Bean
  public Supplier<Flux<String>> outSample1(SampleStringChannel channel) {
    return () -> channel.flux()
        .doOnNext(m -> log.info("Sending message outSample1 : {}", m))
        .doOnError(t -> log.error("Error encountered outSample1 : ", t));
  }

  @Bean
  public Supplier<Flux<Map<String, String>>> outSample2(SampleMapChannel channel) {
    return () -> channel.flux()
        .doOnNext(m -> log.info("Sending message outSample2 : {}", m))
        .doOnError(t -> log.error("Error encountered outSample2", t));
  }

  @Bean
  public ApplicationRunner applicationRunner(StreamBridge streamBridge) {
    return args -> {
      log.info("invoke applicationRunner");
      streamBridge.send("outSample1-out-0", "applicationRunner...");
    };
  }
}
