package com.ask.springwebflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@Slf4j
class MonoTest {

  @Test
  void doOnNext() {
    Mono<Integer> mono = Mono.just(10)
        .doOnNext(i -> log.info("doOnNext : {}", i));

    mono.subscribe(i -> log.info("subscribe : {}", i));
  }
}
