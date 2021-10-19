package com.ask.javacore;

import com.ask.javacore.common.BaseTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class CountDownLatchTest extends BaseTest {

  private static final int COUNT = 5;

  @Order(1)
  @Test
  void countDownLatch() throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(COUNT);
    ExecutorService service = Executors.newFixedThreadPool(COUNT);

    for (int i = 1; i <= COUNT; i++) {
      int finalI = i;
      service.submit(() -> {
        if (finalI == 5) {
          TimeUnit.SECONDS.sleep(2);
        }

        print(finalI, "CALL BEFORE countDown()");
        countDownLatch.countDown();
        print(finalI, "CALL AFTER countDown()");

        return 1;
      });
    }

    countDownLatch.await();
    print("Done");

    service.shutdown();
  }
}
