package com.ask.springredis.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.timeout;

import java.time.Duration;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class RedisKeyExpiredEventListenerTest {

  /**
   * @see org.springframework.data.redis.core.ValueOperationsEditor
   */
  @Resource(name="stringRedisTemplate")
  private ValueOperations<String, String> valueOperations;

  @SpyBean
  private RedisKeyExpiredEventListener redisKeyExpiredEventListener;

  @Test
  void handleEvent() {
    // given
    String key = "1.0.0:type::redis";
    String value = "use";

    // when
    valueOperations.set(key, value, Duration.ofMillis(500));

    // then
    then(redisKeyExpiredEventListener).should(timeout(1000).only()).handleEvent(any());
  }

}
