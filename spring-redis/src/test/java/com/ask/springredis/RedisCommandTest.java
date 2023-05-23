package com.ask.springredis;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@SpringBootTest
@Slf4j
class RedisCommandTest {

  @Autowired
  private RedisConnectionFactory redisConnectionFactory;

  /**
   * actuator 사용시 RedisHealthIndicator 에서 체크함
   */
  @Test
  void version() {
    Properties infos = redisConnectionFactory.getConnection().info("server");
    String redisVersion = (String) infos.get("redis_version");
    log.info("redisVersion: {}", redisVersion);
  }

  @RepeatedTest(10)
  void time() {
    Long time = redisConnectionFactory.getConnection().time();
    log.info("redisTime: {}", time);
    log.info("serverTime: {}", System.currentTimeMillis());
  }

}
