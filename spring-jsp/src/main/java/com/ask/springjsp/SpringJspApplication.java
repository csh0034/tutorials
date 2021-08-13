package com.ask.springjsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 만약 멀티 모듈 구성일 경우 인텔리제이 Edit Configuration 선택후
 * Working directory $MODULE_WORKING_DIR$ 추가 해야함
 */
@SpringBootApplication
public class SpringJspApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(SpringJspApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SpringJspApplication.class);
  }
}
