package com.ask.springcachejcache.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

  @Cacheable(value = "current-millis", key = "#key")
  public String getCachedCurrentMillis(String key) {
    return System.currentTimeMillis() + " : " + key;
  }

  @Cacheable(value = "random-list", key = "#key")
  public List<RandomVO> getRandomList(String key) {
    return ThreadLocalRandom.current()
        .ints(10)
        .mapToObj(RandomVO::new)
        .collect(toList());
  }

  @AllArgsConstructor
  @ToString
  public static class RandomVO {
    private int random;
  }
}
