package com.ask.stream.web;

import com.ask.stream.config.SampleMapChannel;
import com.ask.stream.config.SampleStringChannel;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks.EmitFailureHandler;

@RestController
@RequiredArgsConstructor
public class StreamController {

  private final SampleStringChannel sampleStringChannel;
  private final SampleMapChannel sampleMapChannel;

  @GetMapping("/1")
  public Mono<String> stream1(@RequestParam(defaultValue = "내용") String message) {
    return Mono.just(message)
        .doOnNext(s -> {
          LocalDateTime now = LocalDateTime.now();
          sampleStringChannel.sink().emitNext(s + " : " + now, EmitFailureHandler.FAIL_FAST);
        });
  }

  @GetMapping("/2")
  public Mono<String> stream2(@RequestParam(defaultValue = "내용") String message) {
    return Mono.just(message)
        .doOnNext(s -> {
          Map<String, String> map = new LinkedHashMap<>();
          map.put("content", s + " : " + LocalDateTime.now());
          sampleMapChannel.sink().emitNext(map, EmitFailureHandler.FAIL_FAST);
        });
  }
}
