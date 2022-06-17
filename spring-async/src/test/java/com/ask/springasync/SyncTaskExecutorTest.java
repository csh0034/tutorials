package com.ask.springasync;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ask.springasync.service.AsyncService;
import com.ask.springasync.service.SampleService;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;

@SpringBootTest("spring.main.allow-bean-definition-overriding=true")
public class SyncTaskExecutorTest {

  @Autowired
  private SampleService sampleService;

  @SpyBean
  private AsyncService asyncService;

  @Test
  void printTimestampLog() {
    sampleService.run();

    then(asyncService).should(times(1)).printTimestampLog();
  }

  @TestConfiguration
  public static class TestConfig {

    @Bean
    public Executor asyncExecutor() {
      return new SyncTaskExecutor();
    }

  }

}
