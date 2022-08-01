package com.ask.springaop.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import com.ask.springaop.aspect.LoggerAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@Slf4j
class SampleServiceTest {

  @Autowired
  private SampleService sampleService;

  @SpyBean
  private LoggerAspect loggerAspect;

  @Test
  void sleep() throws Throwable {
    String result = sampleService.sleep(500);
    log.info("result: {}", result);

    then(loggerAspect).should(atLeastOnce()).elapsed(any());
  }

  @DisplayName("부모클래스 메서드의 경우 패키지가 다를 경우 aop 가 동작하지 않는다")
  @Test
  void invoke() throws Throwable {
    String result = sampleService.invoke();
    log.info("result: {}", result);

    then(loggerAspect).should(never()).elapsed(any());
  }

}
