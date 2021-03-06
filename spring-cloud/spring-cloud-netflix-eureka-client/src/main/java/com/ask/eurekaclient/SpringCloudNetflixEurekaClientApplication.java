package com.ask.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableEurekaClient classpath 에 있을 경우 자동 구성됨
public class SpringCloudNetflixEurekaClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudNetflixEurekaClientApplication.class, args);
  }

}
