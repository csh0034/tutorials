package com.ask.springtestcore.contextcaching;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "sample.key=sampleKey2")
@Slf4j
class PropertySecondTest {

  @Autowired
  private Environment env;

  @Test
  void test() {
    log.info("invoke PropertySecondTest.test");
    log.info("sample.key : {}", env.getProperty("sample.key"));
  }
}
