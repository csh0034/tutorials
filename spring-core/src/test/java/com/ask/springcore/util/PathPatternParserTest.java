package com.ask.springcore.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.util.pattern.PatternParseException;

public class PathPatternParserTest {

  private final PathPatternParser parser = new PathPatternParser();

  @DisplayName("입력받은 path 에 대한 pattern 검증")
  @Test
  void parse() {
    // given
    PathPattern pattern = parser.parse("/test/**");
    PathContainer pathContainer = PathContainer.parsePath("/test/project/other/spring");

    // when
    boolean matches = pattern.matches(pathContainer);

    // then
    assertThat(matches).isTrue();
  }

  @DisplayName("{*...} or ** pattern 뒤에 path 가 있을 경우 예외 발생")
  @Test
  void invalidPattern() {
    assertThatExceptionOfType(PatternParseException.class)
        .isThrownBy(() -> parser.parse("/test/**/spring"));
  }
}
