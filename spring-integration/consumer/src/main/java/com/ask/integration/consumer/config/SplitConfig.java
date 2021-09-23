package com.ask.integration.consumer.config;

import java.util.Arrays;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;

/**
 * split() : 기본적으로 페이로드가 Iterable, Iterator, Array, Stream react Publisher 인 경우 각 항목을 개별 메시지로 출력
 *           단일 객체일 경우 무시됨
 * aggregate() : 개별 메세지 시퀀스를 단일 메세지로 집계한다
 */
@Configuration
@Slf4j
public class SplitConfig {

  @Autowired
  private Upcase upcase;

  //@Bean
  public ApplicationRunner applicationRunner() {
    return args -> {
      Collection<String> values = upcase.upcaseList(Arrays.asList("aaa", "bbb", "ccc"));
      log.info("values : {}", values);

      String value = upcase.upcase("aaa");
      log.info("values : {}", value);
    };
  }

  @MessagingGateway(defaultRequestChannel = "upcase.input")
  public interface Upcase {

    @Gateway
    Collection<String> upcaseList(Collection<String> strings);

    @Gateway
    String upcase(String string);
  }

  // sub-flow definition 으로 선언시 기본적으로 채널 이름은 bean name + .input
  @Bean
  public IntegrationFlow upcase() {
    return f -> f
        .split()
        .<String, String>transform(String::toUpperCase)
        .aggregate();
  }
}
