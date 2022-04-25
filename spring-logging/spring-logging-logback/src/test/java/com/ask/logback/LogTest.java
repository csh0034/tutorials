package com.ask.logback;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

@SpringBootTest
@Slf4j
class LogTest {

  @DisplayName("로그 출력")
  @Test
  void log() {
    log.trace("Trace");
    log.debug("Debug");
    log.info("Info");
    log.warn("Warn");
    log.error("Error");
  }

  @DisplayName("MaskingPatternLayout 에 의해 마스킹된 json 로그 검증")
  @Test
  void logMaskedJson() throws IOException {
    // given
    InputStream inputStream = new ClassPathResource("data.json").getInputStream();

    // when
    String json = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    // then
    log.info("\n{}", json);
  }

}
