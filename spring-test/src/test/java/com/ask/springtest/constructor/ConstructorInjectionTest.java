package com.ask.springtest.constructor;

import com.ask.springtest.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
@Slf4j
class ConstructorInjectionTest {

  private final SampleService sampleService;

  @Test
  void test() {
    log.info("{}", sampleService.formatParam(getClass().getSimpleName()));
  }
}
