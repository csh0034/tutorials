package com.ask.springwebflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
class FluxTest {

  @Test
  void doOnNext() {
    Flux<Integer> flux = Flux.just(10, 20)
        .doOnNext(i -> log.info("doOnNext : {}", i));

    flux.subscribe(i -> log.info("subscribe : {}", i));
  }
}
