package com.ask.stream.config;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Component
public class SampleStringChannel {

  private final Sinks.Many<String> value = Sinks.many().multicast().onBackpressureBuffer();

  public Many<String> sink() {
    return this.value;
  }

  public Flux<String> flux() {
    return this.value.asFlux();
  }
}
