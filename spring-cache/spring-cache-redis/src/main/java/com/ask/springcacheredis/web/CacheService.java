package com.ask.springcacheredis.web;

import com.ask.springcacheredis.config.CachedDatabaseConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

  @Cacheable(value = "dto", key = "#dto.timestamp")
  public TestDto get(TestDto dto) {
    log.info("invoke..");
    return dto;
  }

  @NoArgsConstructor
  @AllArgsConstructor(staticName = "of")
  @Getter
  @ToString
  public static class TestDto {
    private long timestamp;
    private String message;
  }

}
