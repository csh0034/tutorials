package com.ask.springr2dbc.repository;

import com.ask.springr2dbc.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
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
    userRepository.save(User.create("ASk", 29)).subscribe();

    userRepository.findAll()
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }

}
