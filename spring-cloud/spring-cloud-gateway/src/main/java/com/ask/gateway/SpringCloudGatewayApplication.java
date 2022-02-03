package com.ask.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCloudGatewayApplication {

  public static void main(String[] args) {
    System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
    SpringApplication.run(SpringCloudGatewayApplication.class, args);
  }
}
