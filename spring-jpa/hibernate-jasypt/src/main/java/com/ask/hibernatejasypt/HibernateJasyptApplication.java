package com.ask.hibernatejasypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HibernateJasyptApplication {

  public static void main(String[] args) {
    SpringApplication.run(HibernateJasyptApplication.class, args);
  }

}
