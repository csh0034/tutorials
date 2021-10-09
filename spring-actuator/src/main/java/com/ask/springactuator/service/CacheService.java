package com.ask.springactuator.service;

import java.time.LocalDateTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

  public static final String CURRENT_MILLIS = "current-millis";

  @Cacheable(value = CURRENT_MILLIS, key = "#key")
  public LocalDateTime getNow(String key) {
    return LocalDateTime.now();
  }
}
