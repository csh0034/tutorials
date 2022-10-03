package com.ask.springcore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.format.support.FormattingConversionService;

@WebMvcTest
public class FormattingConversionServiceTest {

  @Autowired
  private FormattingConversionService mvcConversionService;

  @ParameterizedTest
  @ValueSource(strings = {"1", "222"})
  void convert(String source) {
    assertThat(mvcConversionService.convert(source, Integer.class)).isEqualTo(Integer.parseInt(source));
  }

}
