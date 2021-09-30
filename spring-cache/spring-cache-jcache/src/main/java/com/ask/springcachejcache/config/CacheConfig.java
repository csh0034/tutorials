package com.ask.springcachejcache.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public JCacheManagerCustomizer jCacheManagerCustomizer() {
    return cacheManager -> {
      cacheManager.createCache("current-millis", getConfiguration(2, Duration.ofSeconds(2)));
      cacheManager.createCache("random-list", getConfiguration(10, Duration.ofSeconds(2)));
    };
  }

  private javax.cache.configuration.Configuration<String, Object> getConfiguration(long heap, Duration ttl) {
    return Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder.newCacheConfigurationBuilder(
            String.class, Object.class, ResourcePoolsBuilder.heap(heap))
        .withSizeOfMaxObjectSize(1000, MemoryUnit.B)
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
        .build());
  }
}
