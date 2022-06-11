package com.ask.testcontainers;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.ask.testcontainers.entity.User;
import com.ask.testcontainers.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest("spring.datasource.url=jdbc:tc:mariadb:10.4:///sample")
@Testcontainers
@ActiveProfiles("test")
class MariaDBContainerTest {

  @Autowired
  private UserRepository userRepository;

  @DisplayName("영어 정상 동작")
  @Test
  void save() {
    assertThatNoException().isThrownBy(() -> userRepository.save(User.create("ask", 10)));
  }

  @DisplayName("기본 mariaDB 이미지이기 때문에 character-set 설정이 안되어있어 한글 입력시 예외 발생함")
  @Test
  void saveException() {
    assertThatExceptionOfType(InvalidDataAccessResourceUsageException.class).isThrownBy(
        () -> userRepository.save(User.create("둘리", 10)));
  }

}
