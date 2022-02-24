package com.ask.springtestcore.contextcaching;

import com.ask.springtestcore.config.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WebConfig.class)
@Slf4j
class ContextClassesTest {

  @Autowired
  private String sampleTitle;

  @Test
  void test() {
    log.info("invoke ContextClassesTest.test, {}", sampleTitle);
  }
}
