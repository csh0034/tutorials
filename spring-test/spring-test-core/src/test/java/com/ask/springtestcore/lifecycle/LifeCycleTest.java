package com.ask.springtestcore.lifecycle;

import com.ask.springtestcore.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class LifeCycleTest {

  @Autowired
  private SampleService sampleService;

  @BeforeAll
  static void beforeAll(@Autowired SampleService sampleService) {
    log.info("beforeAll unixTimestamp: {}", sampleService.unixTimestamp());
  }

  @BeforeEach
  void setUp() {
    log.info("setUp unixTimestamp: {}", sampleService.unixTimestamp());
  }

  @Test
  void test1() {
    log.info("test1 unixTimestamp: {}", sampleService.unixTimestamp());
  }

  @Test
  void test2() {
    log.info("test2 unixTimestamp: {}", sampleService.unixTimestamp());
  }

  @AfterEach
  void tearDown() {
    log.info("tearDown unixTimestamp: {}", sampleService.unixTimestamp());
  }

  @AfterAll
  static void afterAll(@Autowired SampleService sampleService) {
    log.info("afterAll unixTimestamp: {}", sampleService.unixTimestamp());
  }

}
