package com.ask.hibernatejasypt.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.hibernatejasypt.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void findAll() {
    userRepository.findAll()
        .forEach(user -> log.info("user: {}", user));
  }

  @DisplayName("검색조건에 사용할 경우 원래 문자열 및 암호화된 문자열 모두 조회 안됨")
  @Test
  void findByPassword() {
    assertAll(
        () -> assertThat(userRepository.findByData("1234")).isNull(),
        () -> assertThat(userRepository.findByData("usZLPCiDsM0QfN0pPhH/jg==")).isNull()
    );
  }

  @DisplayName("LenientPBStringEncryptor 적용시 DB에 암호화 안되어 있을 경우 그대로 조회")
  @Test
  void find() {
    User user = userRepository.findById("user01").orElseThrow(IllegalStateException::new);

    log.info("{}", user);
    assertThat(user.getData()).isEqualTo("non-encrypt");
  }

}

