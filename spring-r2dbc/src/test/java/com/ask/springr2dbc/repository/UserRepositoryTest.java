package com.ask.springr2dbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.springr2dbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Slf4j
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll().subscribe();
  }

  @Test
  void findAll() {
    userRepository.findAll()
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void findAllAfterInsertUser() {
    // given
    String name = "ASk";
    int age = 29;

    // when
    userRepository.save(User.create(name, age)).subscribe();

    // then
    userRepository.findAll()
        .as(StepVerifier::create)
        .assertNext(user -> {
          log.info("user : {}", user);

          assertAll(
              () -> assertThat(user.getName()).isEqualTo(name),
              () -> assertThat(user.getAge()).isEqualTo(age)
          );
        })
        .verifyComplete();
  }

}
