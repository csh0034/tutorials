package com.ask.testcontainers;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.ask.testcontainers.entity.User;
import com.ask.testcontainers.repository.UserRepository;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles({"test", "compose"})
@Slf4j
class DockerComposeMariaDBContainerTest {

  @Container
  static DockerComposeContainer<?> container = new DockerComposeContainer<>(
      new File("src/test/resources/mariadb/docker-compose.yml"))
      .withExposedService("mariadb_1", 3306);

  @Autowired
  private UserRepository userRepository;

  @DisplayName("docker compose 로 실행시 한글 정상 동작")
  @Test
  void save() {
    assertThatNoException().isThrownBy(() -> userRepository.save(User.create("둘리", 10)));
  }

  @DynamicPropertySource
  static void mariaDBProperties(DynamicPropertyRegistry registry) {
    registry.add("mariadb.port", () -> container.getServicePort("mariadb_1", 3306));
  }

}
