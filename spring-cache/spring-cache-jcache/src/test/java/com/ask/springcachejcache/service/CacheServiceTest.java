package com.ask.springcachejcache.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springcachejcache.service.CacheService.RandomVO;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheServiceTest {

  @Autowired
  CacheService cacheService;

  @DisplayName("TTL 2초, 바로 호출")
  @Test
  void getCachedCurrentMillis() {
    // given
   String key = "testKey";

    // when
    String cachedCurrentMillis1 = cacheService.getCachedCurrentMillis(key);
    String cachedCurrentMillis2 = cacheService.getCachedCurrentMillis(key);

    // then
    assertThat(cachedCurrentMillis1).isEqualTo(cachedCurrentMillis2);
  }

  @DisplayName("TTL 2초, 중간에 3초 sleep")
  @Test
  void getCachedCurrentMillisWithSleep() throws InterruptedException {
    // given
    String key = "testKey";

    // when
    String cachedCurrentMillis1 = cacheService.getCachedCurrentMillis(key);
    TimeUnit.SECONDS.sleep(3);
    String cachedCurrentMillis2 = cacheService.getCachedCurrentMillis(key);

    // then
    assertThat(cachedCurrentMillis1).isNotEqualTo(cachedCurrentMillis2);
  }

  @DisplayName("TTL 2초, value List 사용")
  @Test
  void getRandomList() {
    // given
    String key = "testKey";

    // when
    List<RandomVO> randomList1 = cacheService.getRandomList(key);
    List<RandomVO> randomList2 = cacheService.getRandomList(key);

    // then
    assertThat(randomList1).isEqualTo(randomList2);
  }
}