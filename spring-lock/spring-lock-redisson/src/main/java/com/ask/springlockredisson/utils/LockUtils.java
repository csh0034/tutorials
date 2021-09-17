package com.ask.springlockredisson.utils;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockUtils {

  private final RedissonClient redissonClient;

  public boolean isExecuted(String key) {
    RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
    return atomicLong.isExists();
  }

  public void setExecuted(String key, int expireSeconds) {
    RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
    atomicLong.incrementAndGet();
    if (atomicLong.remainTimeToLive() < 0) {
      atomicLong.expire(expireSeconds, TimeUnit.SECONDS);
    }
  }
}
