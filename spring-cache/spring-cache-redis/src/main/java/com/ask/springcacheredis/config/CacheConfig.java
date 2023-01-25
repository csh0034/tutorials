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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

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

  /**
   * .transactionAware() 설정시, 트랜잭션 내부일 경우 commit 후에 put 을 실행한다.
   *
   * @see org.springframework.cache.transaction.TransactionAwareCacheDecorator
   */
  @Bean
  public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
    return builder -> builder.cacheDefaults(redisExpiresConfiguration(cacheProperties.getRedis().getTimeToLive()))
        .withInitialCacheConfigurations(cacheExpiresMap());
  }

  private Map<String, RedisCacheConfiguration> cacheExpiresMap() {
    Map<String, RedisCacheConfiguration> expiresMap = new HashMap<>();
    expiresMap.put(CacheConstants.COMPANY, redisExpiresConfiguration(Duration.ofMinutes(1)));
    expiresMap.put(CacheConstants.DB_PROPERTY, redisExpiresConfiguration(Duration.ofMinutes(10)));
    return expiresMap;
  }

  /**
   * key: String, value: json
   * value 가 String 일 경우 그대로 처리된다.
   */
  private RedisCacheConfiguration redisExpiresConfiguration(Duration ttl) {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
        .prefixCacheNameWith(cacheProperties.getRedis().getKeyPrefix())
        .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(ttl);
  }

}
