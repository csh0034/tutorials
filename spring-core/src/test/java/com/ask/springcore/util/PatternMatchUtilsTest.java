package com.ask.springcore.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.util.PatternMatchUtils;

class PatternMatchUtilsTest {

  @ParameterizedTest
  @CsvSource({
      "*,     com.ask.Class, true",
      "com.*, com.ask.Class, true",
      "com.*, kr.co.ask.Class, false",
  })
  void matches(String pattern, String className, boolean result) {
    assertThat(PatternMatchUtils.simpleMatch(pattern, className)).isEqualTo(result);
  }

}
