package com.ask.javacore;

import com.ask.javacore.common.BaseTest;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class CyclicBarrierTest extends BaseTest {

  private static final int COUNT = 5;

  @Order(1)
  @Test
  void cyclicBarrier() throws Exception {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(COUNT);
    ExecutorService service = Executors.newFixedThreadPool(COUNT);

    for (int i = 1; i <= COUNT; i++) {
      int finalI = i;
      service.submit(() -> {
        if (finalI == 5) {
          TimeUnit.SECONDS.sleep(2);
        }

        print(finalI, "CALL BEFORE await()");
        cyclicBarrier.await();
        print(finalI, "CALL AFTER await()");

        return 1;
      });
    }

    TimeUnit.SECONDS.sleep(3);
    print("Done");

    service.shutdown();
  }
}
