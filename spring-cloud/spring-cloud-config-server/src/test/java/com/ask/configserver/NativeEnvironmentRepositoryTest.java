package com.ask.configserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.NativeEnvironmentRepository;

@SpringBootTest
@Slf4j
class NativeEnvironmentRepositoryTest {

  @Autowired
  private NativeEnvironmentRepository repo;

  @Test
  void locationPlaceholdersMultipleApplication() {
    // 지정하지 않을 경우 NativeEnvironmentRepository DEFAULT_LOCATIONS 사용
    // repo.setSearchLocations("classpath:/config/");
    Environment e = repo.findOne("core,api", "dev", null);

    List<PropertySource> propertySources = e.getPropertySources();
    assertThat(propertySources).hasSize(3);

    propertySources.forEach(
        propertySource -> propertySource.getSource().forEach((key, value) -> log.info("{} : {}", key, value)));
  }

}
