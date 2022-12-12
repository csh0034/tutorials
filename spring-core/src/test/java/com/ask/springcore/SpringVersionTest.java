package com.ask.springcore;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.SpringVersion;

@Slf4j
class SpringVersionTest {

  @Test
  void version() {
    String version = SpringVersion.getVersion();
    log.info("version: {}", version);
  }

}
