package com.ask.springtest.contextcaching;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class NormalTest {

  @Nested
  class TestA {

    @Test
    void test() {
      log.info("invoke TestA.test");
    }
  }

  @Nested
  class TestB {

    @Test
    void test() {
      log.info("invoke TestB.test");
    }
  }
}
