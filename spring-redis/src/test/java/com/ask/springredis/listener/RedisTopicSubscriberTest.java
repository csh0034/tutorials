package com.ask.springredis.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.timeout;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class RedisTopicSubscriberTest {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @SpyBean
  private RedisTopicSubscriber redisTopicSubscriber;

  @Test
  void handle() throws Exception {
    // given
    String topic = "topic-sample";
    UserMessage userMessage = new UserMessage("ask", 10);

    // when
    stringRedisTemplate.convertAndSend(topic, objectMapper.writeValueAsString(userMessage));

    // then
    then(redisTopicSubscriber).should(timeout(1000).times(1)).handle(any());
  }

}
