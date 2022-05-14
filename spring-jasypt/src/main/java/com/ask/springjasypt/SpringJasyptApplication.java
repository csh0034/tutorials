package com.ask.springjasypt;

import com.ask.springjasypt.config.CustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class SpringJasyptApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringJasyptApplication.class, args);
  }

  @Bean
  public ApplicationRunner applicationRunner(CustomProperties customProperties) {
    return args -> log.info("customProperties: {}", customProperties);
  }

}
