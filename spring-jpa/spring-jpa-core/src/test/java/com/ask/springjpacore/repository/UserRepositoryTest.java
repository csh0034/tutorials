package com.ask.springjpacore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpacore.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@Slf4j
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @DisplayName("User 생성")
  @Test
  void create() {
    // given
    User user = User.create("ask", "1234");

    // when
    User savedUser = userRepository.saveAndFlush(user);

    // then
    log.info("user : {}", user);
    assertThat(savedUser.getId()).isNotEmpty();
    assertThat(savedUser).isEqualTo(user);
  }

  @DisplayName("User 생성")
  @Test
  void update() {
    // given
    User user = User.create("ask", "1234");
    userRepository.save(user);

    // when then
    user.updatePassword("5555");
    testEntityManager.flush();
  }
  
}
