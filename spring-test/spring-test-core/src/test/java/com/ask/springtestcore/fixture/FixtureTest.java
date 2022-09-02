package com.ask.springtestcore.fixture;

import com.ask.springtestcore.fixture.User.UserBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class FixtureTest {

  private UserBuilder userBuilder;

  @BeforeEach
  void setUp() {
    userBuilder = User.builder()
        .team("Team A")
        .countryCode("kr");
  }

  @Test
  void fixture1() {
    User user = userBuilder.name("nameA")
        .age(10)
        .build();

    log.info("user: {}", user);
  }

  @Test
  void fixture2() {
    User user = userBuilder.name("nameB")
        .age(20)
        .build();

    log.info("user: {}", user);
  }

}
