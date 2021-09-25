package com.ask.springdbunit.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class LogTest {

  @Test
  void log() {
    log.info("LogTest.log");
  }
}
