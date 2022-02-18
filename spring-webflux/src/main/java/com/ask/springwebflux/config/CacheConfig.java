package com.ask.springwebflux.config;

import com.ask.springwebflux.config.cache.CacheConstants;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
    return builder -> builder.withInitialCacheConfigurations(cacheExpiresMap());
  }

  private Map<String, RedisCacheConfiguration> cacheExpiresMap() {
    Map<String, RedisCacheConfiguration> expiresMap = new HashMap<>();
    expiresMap.put(CacheConstants.COMPANY, redisExpiresConfiguration(30));
    expiresMap.put(CacheConstants.COMPANIES, redisExpiresConfiguration(20));
    return expiresMap;
  }

  private RedisCacheConfiguration redisExpiresConfiguration(long ttlSecond) {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
        .entryTtl(Duration.ofSeconds(ttlSecond));
  }

}
