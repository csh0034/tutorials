package com.ask.shedlock;

import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LockRunner implements ApplicationRunner {

  private final LockingTaskExecutor lockingTaskExecutor;

  @Override
  public void run(ApplicationArguments args) {
    String lockKey = DigestUtils.sha1Hex("key..");

    lockingTaskExecutor.executeWithLock((Runnable) () -> {
      log.info("LockRunner start...");

      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      log.info("LockRunner end...");
    }, new LockConfiguration(Instant.now(), lockKey, Duration.ofSeconds(10), Duration.ofSeconds(10)));
  }

}
