package com.ask.springjpacache.config;

import javax.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {

  private final CacheManager cacheManager;

  @Bean
  public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer() {
    return (properties) -> properties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
  }
}
