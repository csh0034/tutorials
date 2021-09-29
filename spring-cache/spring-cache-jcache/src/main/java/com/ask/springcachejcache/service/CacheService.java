package com.ask.springcachejcache.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

  @Cacheable(value = "current-millis", key = "#key")
  public String getCachedCurrentMillis(String key) {
    return System.currentTimeMillis() + " : " + key;
  }
}
