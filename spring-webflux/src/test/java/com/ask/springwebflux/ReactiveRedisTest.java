package com.ask.springwebflux;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.test.StepVerifier;

@DataRedisTest
public class ReactiveRedisTest {

  @Autowired
  private ReactiveStringRedisTemplate template;

  private ReactiveValueOperations<String, String> valueOperations;

  @BeforeEach
  void setUp() {
    valueOperations = template.opsForValue();
  }

  @Test
  void setAndGet() {
    String cacheKey = "type";
    String cacheValue = "Redis";

    valueOperations.set(cacheKey, cacheValue, Duration.ofSeconds(30))
        .as(StepVerifier::create)
        .expectNext(true)
        .verifyComplete();

    valueOperations.get(cacheKey)
        .as(StepVerifier::create)
        .expectNext(cacheValue)
        .verifyComplete();
  }

}
