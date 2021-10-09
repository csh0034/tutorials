package com.ask.springactuator.config;

import com.ask.springactuator.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

  private final CacheService cacheService;

  @Scheduled(fixedRateString = "PT5S")
  public void callCachedMethod() {
    log.info("Cached LocalDateTime : {}", cacheService.getNow("key"));
  }
}
