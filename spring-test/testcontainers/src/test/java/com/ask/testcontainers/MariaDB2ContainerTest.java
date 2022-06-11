package com.ask.testcontainers;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.ask.testcontainers.entity.User;
import com.ask.testcontainers.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class MariaDB2ContainerTest {

  @Container
  static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:10.4")
      .withCommand("--character-set-server=utf8mb4",
          "--collation-server=utf8mb4_unicode_ci",
          "--lower_case_table_names=1");

  @Autowired
  private UserRepository userRepository;

  @DisplayName("영어 저장 정상 동작")
  @Test
  void save() {
    assertThatNoException().isThrownBy(() -> userRepository.save(User.create("ask", 10)));
  }

  @DisplayName("한글 저장 정상 동작")
  @Test
  void saveException() {
    assertThatNoException().isThrownBy(() -> userRepository.save(User.create("둘리", 10)));
  }

  @DynamicPropertySource
  static void mariaDBProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mariaDB::getJdbcUrl);
    registry.add("spring.datasource.username", mariaDB::getUsername);
    registry.add("spring.datasource.password", mariaDB::getPassword);
  }

}
