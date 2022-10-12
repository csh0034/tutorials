package com.ask.springcore;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springcore.converter.TestType;
import org.junit.jupiter.api.DisplayName;
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

  @DisplayName("대소문자 구분안함, 특수문자 제거 처리후 비교")
  @ParameterizedTest
  @ValueSource(strings = {"A_TYPE", "a_type", "atype", "a$type", "a^type"})
  void convertStringToEnum(String source) {
    // when
    TestType result = mvcConversionService.convert(source, TestType.class);

    // then
    assertThat(result).isSameAs(TestType.A_TYPE);
  }

}
