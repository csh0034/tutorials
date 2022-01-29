package com.ask.stream.web;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;

@RestController
@RequiredArgsConstructor
public class StreamController {

  private final Sinks.Many<String> many1;
  private final Sinks.Many<Map<String, String>> many2;

  @GetMapping("/1")
  public Mono<String> stream1(@RequestParam(defaultValue = "메세지") String message) {
    return Mono.just(message)
        .doOnNext(s -> {
          LocalDateTime now = LocalDateTime.now();
          many1.emitNext(s + " : " + now, EmitFailureHandler.FAIL_FAST);
        });
  }

  @GetMapping("/2")
  public Mono<String> stream2(@RequestParam(defaultValue = "메세지") String message) {
    return Mono.just(message)
        .doOnNext(s -> {
          Map<String, String> map = new LinkedHashMap<>();
          map.put("content", s + " : " + LocalDateTime.now());
          many2.emitNext(map, EmitFailureHandler.FAIL_FAST);
        });
  }
}
