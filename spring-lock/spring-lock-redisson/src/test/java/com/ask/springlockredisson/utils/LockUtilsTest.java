package com.ask.springlockredisson.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class LockUtilsTest {

  private static final int THREAD_SIZE = 10;

  CountDownLatch countDownLatch = new CountDownLatch(THREAD_SIZE);
  ExecutorService service = Executors.newFixedThreadPool(THREAD_SIZE);

  @Autowired
  LockUtils lockUtils;

  @Autowired
  RedissonClient redissonClient;

  @DisplayName("redisson 락을 사용하여 동시 접근시에 한번만 실행")
  @Test
  void redissonLock() throws Exception {
    // given
    String lockKey = "lock-1";

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        lock(lockKey);
        countDownLatch.countDown();
      });
    }
    countDownLatch.await();

    // then
    log.info("end");
  }

  void lock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      if (lock.tryLock(15, 10, TimeUnit.SECONDS)) {
        try {
          String executeKey = lockKey + "_";
          if (lockUtils.isExecuted(executeKey)) {
            log.info("already executed");
          } else {
            log.info("executed!!!");
            lockUtils.setExecuted(executeKey, 10);
          }
        } finally {
          if (lock.isLocked())
            lock.unlock();
        }
      }
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @DisplayName("redisson RAtomicLong 을 사용하여 동시 접근시에 한번만 실행")
  @RepeatedTest(5)
  void redissonRAtomicLong() throws Exception {
    // given
    String lockKey = "lock-2";
    AtomicInteger count = new AtomicInteger(0);

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(lockKey);

        if (atomicLong.compareAndSet(0, 1)) {
          atomicLong.expire(2, TimeUnit.SECONDS);
          count.getAndIncrement();
          log.info("executed!!!");
        } else {
          log.info("already executed");
        }

        countDownLatch.countDown();
      });
    }
    countDownLatch.await();

    // then
    log.info("end");
    Assertions.assertThat(count.get()).isEqualTo(1);
    TimeUnit.SECONDS.sleep(3);
  }
}