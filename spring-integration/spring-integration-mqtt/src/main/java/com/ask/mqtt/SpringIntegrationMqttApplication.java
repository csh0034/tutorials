package com.ask.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringIntegrationMqttApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringIntegrationMqttApplication.class, args);
  }

}
