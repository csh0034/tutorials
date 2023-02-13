package com.ask.springjpajcache.config;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.ask.springjpajcache.entity.Company;
import com.ask.springjpajcache.entity.Role;
import com.ask.springjpajcache.entity.User;
import com.hazelcast.cache.impl.HazelcastServerCachingProvider;
import javax.cache.CacheManager;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class JCacheConfig {

  private final CacheEventLoggerListener cacheEventLoggerListener;

  @Bean
  public CacheManager jCacheCacheManager() {
//    CachingProvider cachingProvider = new EhcacheCachingProvider();
    CachingProvider cachingProvider = new HazelcastServerCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager();

    cacheManager.createCache(User.class.getName(), getConfiguration(new Duration(SECONDS, 5)));
    cacheManager.createCache(Company.class.getName(), getConfiguration(new Duration(SECONDS, 5)));
    cacheManager.createCache(Role.class.getName(), getConfiguration(new Duration(SECONDS, 5)));

    return cacheManager;
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
