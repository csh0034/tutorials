package com.ask.springbatch;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBatchApplication.class, args);
  }

  @Bean
  public ApplicationRunner runner(BatchProperties properties) {
    return args -> System.out.println("spring.batch.job.names : " + properties.getJob().getNames());
  }
}
