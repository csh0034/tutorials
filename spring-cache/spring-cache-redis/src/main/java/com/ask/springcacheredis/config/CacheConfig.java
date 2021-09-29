package com.ask.springcacheredis.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

  private final DataSource dataSource;

  @Bean
  public CachedDatabaseConfiguration config() {
    return new CachedDatabaseConfiguration(dataSource, "zt_config", "id", "value");
  }
}
