package com.ask.stream.config;

import java.util.Map;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Component
public class SampleMapChannel {

  private final Many<Map<String, String>> value = Sinks.many().multicast().onBackpressureBuffer();

  public Many<Map<String, String>> sink() {
    return this.value;
  }

  public Flux<Map<String, String>> flux() {
    return this.value.asFlux();
  }
}
