package com.ask.caffeine.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

  /**
   * removalListener 의 경우 캐시 만료 + cache 에 접근을 해야만 호출된다.
   * @see org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
   */
  @Bean
  public Caffeine<Object, Object> caffeine() {
    return Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .removalListener((key, value, cause) -> log.info("key: {}, value: {}, removal cause: {}", key, value, cause));
  }

}
