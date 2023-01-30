package com.ask.springtestcore;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Slf4j
public class NestedOrderTest {

  @Nested
  @Order(1)
  @SpringBootTest
  @TestPropertySource(properties = "test.drop=true")
  class Drop {

    @Autowired
    private Environment environment;

    @Test
    void drop() {
      log.info("drop");
      log.info("test.drop: {}", environment.getProperty("test.drop"));
      log.info("test.create: {}", environment.getProperty("test.create"));
    }

  }

  @Nested
  @Order(2)
  @SpringBootTest
  @TestPropertySource(properties = "test.create=true")
  class Create {

    @Autowired
    private Environment environment;

    @Test
    void create() {
      log.info("create");
      log.info("test.drop: {}", environment.getProperty("test.drop"));
      log.info("test.create: {}", environment.getProperty("test.create"));
    }

  }

}
