package com.ask.springredis;

import java.time.Duration;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class ValueOperationsTest {

  private static final String KEY = "key";
  private static final String VALUE = "value";

  @Resource(name="stringRedisTemplate")
  private ValueOperations<String, String> valueOperations;

  @DisplayName("SETEX, Available since: 2.0.0")
  @Test
  void set() {
    valueOperations.set(KEY, VALUE, Duration.ofMillis(500));
  }

  @DisplayName("GETDEL, Available since: 6.2.0")
  @Test
  void getAndDelete() {
    valueOperations.getAndDelete(VALUE);
  }

  @DisplayName("DEL, Available since: 1.0.0")
  @Test
  void delete() {
    valueOperations.getOperations().delete(KEY);
  }

}
