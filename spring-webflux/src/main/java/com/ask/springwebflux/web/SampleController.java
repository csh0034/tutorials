package com.ask.springwebflux.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SampleController {

  @GetMapping("/mono")
  public Mono<String> sample() {
    return Mono.just("Hello");
  }

  @GetMapping("/flux")
  public Flux<String> sample2() {
    return Flux.just("Hello", "World");
  }
}