package com.ask.caffeine.controller;

import java.time.LocalDateTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

  @GetMapping("/")
  @Cacheable(value = "CURRENT_MILLIS", key = "#key")
  public String getNow(@RequestParam(defaultValue = "1") String key) {
    return key + " : " + LocalDateTime.now();
  }

}
