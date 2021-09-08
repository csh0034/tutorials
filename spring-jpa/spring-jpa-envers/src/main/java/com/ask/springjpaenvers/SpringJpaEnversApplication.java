package com.ask.springjpaenvers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;

@SpringBootApplication
@EnableEnversRepositories
public class SpringJpaEnversApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringJpaEnversApplication.class, args);
  }

}
