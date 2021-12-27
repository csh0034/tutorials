package com.ask.springtest.contextcaching;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest(properties = "sample.key=sampleKey")
@Slf4j
class PropertyFirstTest {

  @Autowired
  private Environment env;

  @Test
  void test() {
    log.info("invoke PropertyTest.test");
    log.info("sample.key : {}", env.getProperty("sample.key"));
  }
}
