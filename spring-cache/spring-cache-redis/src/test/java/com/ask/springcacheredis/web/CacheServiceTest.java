package com.ask.springcacheredis.web;

import com.ask.springcacheredis.web.CacheService.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CacheServiceTest {

  @Autowired
  private CacheService cacheService;

  @Test
  void get() {
    log.info("{}", cacheService.get(TestDto.of(1, "message1-1...")));
    log.info("{}", cacheService.get(TestDto.of(1, "message1-2...")));
    log.info("{}", cacheService.get(TestDto.of(2, "message2...")));
    log.info("{}", cacheService.getCacheString("a"));
    log.info("{}", cacheService.getCacheString("a"));
  }

}
