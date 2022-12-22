package com.ask.springredis;

import java.time.Duration;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
@Slf4j
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

  @DisplayName("EXISTS, Available since: 1.0.0")
  @Test
  void hasKey() {
    Boolean result = valueOperations.getOperations().hasKey(KEY);
    log.info("result: {}", result);
  }

  @DisplayName("INCRBY, Available since: 1.0.0")
  @Test
  void increment() {
    Long result = valueOperations.increment(KEY);
    log.info("result: {}", result);
  }

  @DisplayName("SETEX + INCRBY")
  @Test
  void setAndIncrement() {
    valueOperations.set(KEY, "1", Duration.ofMinutes(1));
    Long result = valueOperations.increment(KEY);
    log.info("result: {}", result);
  }

  @AfterAll
  static void afterAll(@Autowired StringRedisTemplate stringRedisTemplate) {
    log.info("called");
    stringRedisTemplate.delete(KEY);
  }

}
