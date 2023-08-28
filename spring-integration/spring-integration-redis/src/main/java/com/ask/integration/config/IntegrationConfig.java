package com.ask.integration.config;

import com.ask.integration.config.RedisTopicHandler.RedisTopicMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.redis.inbound.RedisInboundChannelAdapter;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class IntegrationConfig {

  @Bean
  public IntegrationFlow redisTopicFlow(RedisConnectionFactory redisConnectionFactory, RedisTopicHandler handler) {
    RedisInboundChannelAdapter adapter = new RedisInboundChannelAdapter(redisConnectionFactory);
    adapter.setTopics("test.topic");

    return IntegrationFlows.from(adapter)
        .transform(Transformers.fromJson(RedisTopicMessage.class))
        .handle(message -> handler.handleDefault((RedisTopicMessage) message.getPayload()))
        .get();
  }

  @Bean
  public IntegrationFlow redisUserFlow(RedisConnectionFactory redisConnectionFactory, RedisTopicHandler handler) {
    RedisInboundChannelAdapter adapter = new RedisInboundChannelAdapter(redisConnectionFactory);
    adapter.setTopics("test.user");

    return IntegrationFlows.from(adapter)
        .transform(Transformers.fromJson(RedisTopicMessage.class))
        .handle(message -> handler.handleUser((RedisTopicMessage) message.getPayload()))
        .get();
  }

}
