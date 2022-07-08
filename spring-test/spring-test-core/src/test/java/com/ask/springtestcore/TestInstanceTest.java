package com.ask.springtestcore;

import com.ask.springtestcore.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TestInstanceTest {

  @Autowired
  private SampleService sampleService;

  @Test
  void test1() {
    log.info("{}", this);
    log.info("{}", sampleService);
  }

  @Test
  void test2() {
    log.info("{}", this);
    log.info("{}", sampleService);
  }

}
