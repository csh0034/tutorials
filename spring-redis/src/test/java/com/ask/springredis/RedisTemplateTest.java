package com.ask.springredis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@SpringBootTest
class RedisTemplateTest {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  void convertAndSend() {
    redisTemplate.convertAndSend("test", new TestDto("1", "2"));
  }

  @AllArgsConstructor
  @Getter
  static class TestDto {
    private String a;
    private String b;
  }


  @TestConfiguration
  static class TestConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
      RedisTemplate<String, Object> template = new RedisTemplate<>();
      template.setConnectionFactory(redisConnectionFactory);
      template.setKeySerializer(RedisSerializer.string());
      template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//      template.setValueSerializer(RedisSerializer.json());
//      template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Math.class));
      return template;
    }

  }

}
