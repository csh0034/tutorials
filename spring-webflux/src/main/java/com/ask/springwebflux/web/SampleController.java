package com.ask.springwebflux.web;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class SampleController {

  @GetMapping("/mono")
  public Mono<String> sample() {
    return Mono.just("Hello");
  }

  @GetMapping("/flux")
  public Flux<String> sample2() {
    return Flux.just("Hello", "World");
  }

  @GetMapping("/exception1")
  public Mono<String> exception1() {
    throw new RuntimeException("exception1");
  }

  @GetMapping("/exception2")
  public Mono<String> exception2() {
    return Mono.error(new RuntimeException("exception2"));
  }

  @GetMapping("/test")
  public Mono<String> test() {
    return Mono.just("test...")
        .delayElement(Duration.ofSeconds(3));
  }

}
