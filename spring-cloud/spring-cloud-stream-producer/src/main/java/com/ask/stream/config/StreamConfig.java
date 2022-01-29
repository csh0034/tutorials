package com.ask.stream.config;

import java.util.Map;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
@Slf4j
public class StreamConfig {

  @Bean
  public Sinks.Many<String> many1() {
    return Sinks.many().multicast().onBackpressureBuffer();
  }

  @Bean
  public Sinks.Many<Map<String, String>> many2() {
    return Sinks.many().multicast().onBackpressureBuffer();
  }

  @Bean
  public Supplier<Flux<String>> outSample1() {
    return () -> many1().asFlux()
        .doOnNext(m -> log.info("Sending message outSample1 : {}", m))
        .doOnError(t -> log.error("Error encountered outSample1 : ", t));
  }

  @Bean
  public Supplier<Flux<Map<String, String>>> outSample2() {
    return () -> many2().asFlux()
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
