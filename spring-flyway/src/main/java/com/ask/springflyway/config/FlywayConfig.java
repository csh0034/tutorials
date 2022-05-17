package com.ask.springflyway.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

//  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return Flyway::repair;
  }

}
