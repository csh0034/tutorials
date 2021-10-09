package com.ask.springadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SpringAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAdminApplication.class, args);
  }

}
