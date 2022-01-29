package com.ask.springwebflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
class FluxTest {

  @Test
  void doOnNext() {
    Flux<Integer> mono = Flux.just(10, 20)
        .doOnNext(i -> log.info("doOnNext : {}", i));

    mono.subscribe(i -> log.info("subscribe : {}", i));
  }
}
