package com.ask.springcore.qualifier;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QualifierConfig {

  @Bean
  public Animal rabbit() {
    return new Animal("rabbit");
  }

  @Bean
  public Animal lion() {
    return new Animal("lion");
  }

  @Bean
  public ApplicationRunner applicationRunner(@Qualifier("rabbit") Animal animal) {
    return args -> log.info("animal: {}", animal);
  }

  @RequiredArgsConstructor
  @ToString
  public static class Animal {
    private final String name;
  }

}
