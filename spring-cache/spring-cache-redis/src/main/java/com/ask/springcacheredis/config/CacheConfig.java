package com.ask.springcacheredis.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

  private final DataSource dataSource;
  private final CacheProperties cacheProperties;

  @Bean
  public CachedDatabaseConfiguration config() {
    return new CachedDatabaseConfiguration(dataSource, "zt_config", "id", "value");
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
    return builder -> builder.withInitialCacheConfigurations(cacheExpiresMap()).transactionAware();
  }

  private Map<String, RedisCacheConfiguration> cacheExpiresMap() {
    Map<String, RedisCacheConfiguration> expiresMap = new HashMap<>();
    expiresMap.put(CacheConstants.COMPANY, redisExpiresConfiguration(60));
    expiresMap.put(CacheConstants.DB_PROPERTY, redisExpiresConfiguration(60 * 10));
    return expiresMap;
  }

  private RedisCacheConfiguration redisExpiresConfiguration(long ttlSecond) {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
        .prefixCacheNameWith(cacheProperties.getRedis().getKeyPrefix())
        .entryTtl(Duration.ofSeconds(ttlSecond));
  }

}
