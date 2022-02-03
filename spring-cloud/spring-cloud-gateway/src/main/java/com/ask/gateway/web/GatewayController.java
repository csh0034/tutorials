package com.ask.gateway.web;

import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GatewayController {

  @GetMapping("/test")
  public Mono<ResponseEntity<String>> proxy(ProxyExchange<String> proxy) {
    return proxy.uri("http://localhost:9000/test").get();
  }

  @GetMapping("/just")
  public Mono<String> just() {
    return Mono.just("just");
  }
}
