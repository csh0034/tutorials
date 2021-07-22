package com.ask.springcache.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CacheController {

  private final CacheService cacheService;

  @GetMapping("/")
  public String notCache() {
    return cacheService.getNotCacheString();
  }

  @GetMapping("/cache")
  public String cache(@RequestParam(defaultValue = "0") String key) {
    return cacheService.getCacheString(key);
  }

  @GetMapping("/config")
  public String config(@RequestParam(defaultValue = "0") String key) {
    return cacheService.getConfig(key);
  }

  @GetMapping("/config/put")
  public String configPut(@RequestParam(defaultValue = "0") String key, @RequestParam(defaultValue = "0") String value) {
    return cacheService.setConfig(key, value);
  }
}


