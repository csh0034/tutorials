package com.ask.springtest.contextcaching;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@Slf4j
class DirtiesContextTest {

  @Test
  void test() {
    log.info("invoke DirtiesContextTest.test");
  }
}
