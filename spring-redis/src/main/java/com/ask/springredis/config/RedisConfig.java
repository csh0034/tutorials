package com.ask.springredis.config;

import com.ask.springredis.listener.RedisTopicSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
@RequiredArgsConstructor
public class RedisConfig {

  private final RedisConnectionFactory redisConnectionFactory;
  private final RedisTopicSubscriber redisTopicSubscriber;

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
    redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
    redisMessageListenerContainer.addMessageListener(redisTopicSubscriber, topic());
    return redisMessageListenerContainer;
  }

  @Bean
  public Topic topic() {
    return new ChannelTopic("topic-sample");
  }

}
