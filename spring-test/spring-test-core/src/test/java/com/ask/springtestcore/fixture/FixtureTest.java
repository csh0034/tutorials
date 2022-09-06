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
        .countryCode("kr");
  }

  @Test
  void fixture1() {
    User user = userBuilder.name("nameA")
        .age(10)
        .build();

    user.assignTeam("Team A");

    log.info("user: {}", user);
  }

  @Test
  void fixture2() {
    User user = userBuilder.name("nameB")
        .age(20)
        .build();

    user.assignTeam("Team B");

    log.info("user: {}", user);
  }

}
