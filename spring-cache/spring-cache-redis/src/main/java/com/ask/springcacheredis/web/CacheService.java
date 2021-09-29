package com.ask.springcacheredis.web;

import com.ask.springcacheredis.config.CachedDatabaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

  private final CachedDatabaseConfiguration config;

  public String getNotCacheString() {
    return String.valueOf(System.currentTimeMillis());
  }

  @Cacheable(value = "cacheKey")
  public String getCacheString(String key) {
    return System.currentTimeMillis() + " : " + key;
  }

  public String getConfig(String key) {
    return config.getString(key);
  }

  public String setConfig(String key, String value) {
    config.setProperty(key, value);
    return "success";
  }
}
