package com.ask.springjpacore.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpacore.entity.Password;
import com.ask.springjpacore.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void create() {
    // given
    User user = User.create("ask", Password.create("1234"));

    // when
    User savedUser = userService.saveAndFlush(user);

    // then
    log.info("user: {}", user);
    assertThat(savedUser.getId()).isNotEmpty();
    assertThat(savedUser).isEqualTo(user);
  }

}
