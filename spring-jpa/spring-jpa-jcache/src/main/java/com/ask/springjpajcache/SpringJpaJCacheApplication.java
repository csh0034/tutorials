package com.ask.springjpajcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringJpaJCacheApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringJpaJCacheApplication.class, args);
  }

}
