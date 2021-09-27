package com.ask.springdbunit.init;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = "spring.jpa.hibernate.ddl-auto=none")
@Slf4j
class InitDB {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @BeforeAll
  static void beforeAll() {
    if (System.getProperty("db.name") == null) {
      throw new IllegalStateException("system property 'db.name' must not be null");
    };
  }

  @DisplayName("drop database -> create database")
  @Test
  void createDrop() {

    String dbName = System.getProperty("db.name");

    log.info("drop database dbunit");
    jdbcTemplate.execute("drop database " + dbName);

    log.info("create database dbunit");
    jdbcTemplate.execute("create database " + dbName);
  }
}
