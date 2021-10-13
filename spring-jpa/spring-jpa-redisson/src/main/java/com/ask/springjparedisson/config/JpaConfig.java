package com.ask.springjparedisson.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

  @Bean
  public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer() {
    return (properties) -> {
      // cache definition applied to all caches in entity region
      properties.put("hibernate.cache.redisson.entity.eviction.max_entries", "1000");
      properties.put("hibernate.cache.redisson.entity.expiration.time_to_live", "10000");
      properties.put("hibernate.cache.redisson.entity.expiration.max_idle_time", "7000");

      // cache definition applied to all caches in collection region
      properties.put("hibernate.cache.redisson.collection.eviction.max_entries", "1000");
      properties.put("hibernate.cache.redisson.collection.expiration.time_to_live", "10000");
      properties.put("hibernate.cache.redisson.collection.expiration.max_idle_time", "7000");

      // cache definition for entity region. Example region name: "sample_region"
      properties.put("hibernate.cache.redisson.sample_region.eviction.max_entries", "1000");
      properties.put("hibernate.cache.redisson.sample_region.expiration.time_to_live", "1000");
//      properties.put("hibernate.cache.redisson.entity.expiration.max_idle_time", "7000");
    };
  }
}
