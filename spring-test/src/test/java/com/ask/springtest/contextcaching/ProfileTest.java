package com.ask.springtest.contextcaching;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
class ProfileTest {

  @Nested
  @ActiveProfiles("lang_en")
  class English {

    @Test
    void test() {
      log.info("invoke English.test");
    }
  }

  @Nested
  @ActiveProfiles("lang_de")
  class German {

    @Test
    void test() {
      log.info("invoke German.test");
    }
  }
}
