package com.ask.springjpajcache.config;

import static java.util.concurrent.TimeUnit.SECONDS;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

  private final CacheEventLoggerListener cacheEventLoggerListener;

  @Bean
  public JCacheManagerCustomizer jCacheManagerCustomizer() {
    return cacheManager -> {
      cacheManager.createCache("com.ask.springjpacache.entity.User", getConfiguration(new Duration(SECONDS, 5)));
      cacheManager.createCache("com.ask.springjpacache.entity.Company", getConfiguration(new Duration(SECONDS, 5)));
    };
  }

  private MutableConfiguration<String, Object> getConfiguration(Duration duration) {
    MutableConfiguration<String, Object> configuration = new MutableConfiguration<>();
    configuration.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(duration));

    MutableCacheEntryListenerConfiguration<String, Object> listenerConfiguration =
        new MutableCacheEntryListenerConfiguration<>(FactoryBuilder.factoryOf(cacheEventLoggerListener),
            null,
            true,
            true
        );
    configuration.addCacheEntryListenerConfiguration(listenerConfiguration);

    return configuration;
  }
}
