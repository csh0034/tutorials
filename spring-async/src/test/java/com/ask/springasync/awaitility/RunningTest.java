package com.ask.springasync.awaitility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class RunningTest {

  @Test
  void atomicWithAssertJ() {
    // given
    int threadPoolCount = 5;
    AtomicInteger atomic = new AtomicInteger(0);

    ExecutorService executorService = Executors.newFixedThreadPool(threadPoolCount);

    // when
    for (int i = 0; i < threadPoolCount; i++) {
      int timeout = 1000 - i * 200;

      executorService.submit(() -> {
        log.info("sleep start, timeout millis: {}", timeout);
        try {
          Thread.sleep(timeout);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        atomic.incrementAndGet();
      });
    }

    // then
    await().atMost(2, TimeUnit.SECONDS)
        .pollInterval(200, TimeUnit.MILLISECONDS)
        .untilAsserted(() -> {
          log.info("call, atomic value: {}", atomic.get());
          assertThat(atomic).hasValue(threadPoolCount);
        });
  }

}
