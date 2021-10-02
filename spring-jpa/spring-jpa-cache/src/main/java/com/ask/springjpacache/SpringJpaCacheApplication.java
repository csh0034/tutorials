package com.ask.springjpacache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringJpaCacheApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringJpaCacheApplication.class, args);
  }

}
