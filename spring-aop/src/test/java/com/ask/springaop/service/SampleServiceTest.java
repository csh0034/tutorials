package com.ask.springaop.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SampleServiceTest {

  @Autowired
  private SampleService sampleService;

  @Test
  void sleep() {
    String result = sampleService.sleep(500);
    log.info("result: {}", result);
  }

}
