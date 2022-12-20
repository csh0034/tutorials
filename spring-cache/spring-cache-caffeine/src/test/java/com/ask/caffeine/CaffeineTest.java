package com.ask.caffeine;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class CaffeineTest {

  @Test
  void getAndCleanUp() throws InterruptedException {
    LoadingCache<String, String> caches = Caffeine.newBuilder()
        .maximumSize(10)
        .expireAfterWrite(Duration.ofMillis(200))
        .removalListener((RemovalListener<String, String>) (key, value, cause) -> log.info("removal cause: {}", cause))
        .build(key -> {
          log.info("load, key: [{}]", key);
          return key.toUpperCase();
        });

    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.get("aaa")).isEqualTo("AAA");
    assertThat(caches.get("aaa")).isEqualTo("AAA");

    assertThat(caches.estimatedSize()).isOne();

    TimeUnit.MILLISECONDS.sleep(300);

    assertThat(caches.estimatedSize()).isOne();
    caches.cleanUp();
    assertThat(caches.estimatedSize()).isZero();
  }

  @Test
  void put() throws InterruptedException {
    Cache<String, String> caches = Caffeine.newBuilder()
        .maximumSize(10)
        .expireAfterWrite(Duration.ofMillis(200))
        .removalListener((RemovalListener<String, String>) (key, value, cause) -> log.info("removal cause: {}", cause))
        .build();

    caches.put("aaa", "BBB");
    assertThat(caches.getIfPresent("aaa")).isEqualTo("BBB");
    assertThat(caches.estimatedSize()).isOne();

    TimeUnit.MILLISECONDS.sleep(300);

    assertThat(caches.estimatedSize()).isOne();
    caches.cleanUp();
    assertThat(caches.estimatedSize()).isZero();
  }

}
