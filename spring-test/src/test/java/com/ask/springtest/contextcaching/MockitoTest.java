package com.ask.springtest.contextcaching;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ask.springtest.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@Slf4j
class MockitoTest {

  @Nested
  class Mock {

    @MockBean
    private SampleService sampleService;

    @Test
    void test() {
      log.info("invoke MockingTest.test");

      given(sampleService.formatParam(any())).willReturn("mock-stubbing");
      log.info("param : {}", sampleService.formatParam("param"));
    }
  }

  @Nested
  class Spy {

    @SpyBean
    private SampleService sampleService;

    @Test
    void test() {
      log.info("invoke SpyBeanTest.test");

      given(sampleService.formatParam(any())).willReturn("spy-stubbing");
      log.info("param : {}", sampleService.formatParam("param"));
    }
  }
}
