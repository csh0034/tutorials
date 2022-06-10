package com.ask.testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class RedisContainerTest {

  @Container
  public GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine"))
      .withExposedPorts(6379);

  @Test
  public void testRedisContainer() {
    assertThat(redis.getHost()).isEqualTo("localhost");
    assertThat(redis.getFirstMappedPort()).isGreaterThan(0);
  }

}
