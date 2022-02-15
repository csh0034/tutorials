package com.ask.springr2dbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.springr2dbc.config.Transaction;
import com.ask.springr2dbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(Transaction.class)
@Slf4j
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

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

    // when then
    userRepository.save(User.create(name, age))
        .mapNotNull(User::getId)
        .flatMapMany(id -> userRepository.findAll())
        .as(Transaction::withRollback)
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
