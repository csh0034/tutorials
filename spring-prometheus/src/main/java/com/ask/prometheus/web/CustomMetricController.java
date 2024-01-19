package com.ask.prometheus.web;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomMetricController {

  private final MeterRegistry meterRegistry;
  private final AtomicInteger count = new AtomicInteger(10);

  @PostConstruct
  void init() {
    meterRegistry.gauge("custom.gauge", Tags.of("tag", "gauge"), count);
  }

  @GetMapping("/up")
  public String up() {
    meterRegistry.counter("custom.up.count", "tag", "up").increment();
    count.incrementAndGet();
    return "up";
  }

  @GetMapping("/down")
  public String down() {
    Counter counter = Counter.builder("custom.down.count")
        .tag("tag", "down")
        .description("down tag description")
        .register(meterRegistry);
    counter.increment();

    count.decrementAndGet();
    return "down";
  }

}
