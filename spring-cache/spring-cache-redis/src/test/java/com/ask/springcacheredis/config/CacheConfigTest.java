package com.ask.springcacheredis.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

@SpringBootTest
class CacheConfigTest {

  @Autowired
  private CacheManager cacheManager;

  @Test
  void cache() {
    // given
    Cache cache = cacheManager.getCache(CacheConstants.COMPANY);
    assertThat(cache).isNotNull();

    String key = "company01";
    String value = "google";

    // when
    cache.put(key, value);

    // then
    ValueWrapper valueWrapper = cache.get(key);
    assertThat(valueWrapper).isNotNull();
    assertThat(valueWrapper.get()).isEqualTo(value);

    cache.clear();
  }

}
