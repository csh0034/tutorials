package com.ask.springcore.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.ask.springcore.config.converter.Code;
import com.ask.springcore.config.converter.LenientStringToEnumConverterFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.convert.converter.Converter;

class LenientStringToEnumConverterFactoryTest {

  private Converter<String, TestType> converter;

  @BeforeEach
  void setUp() {
    LenientStringToEnumConverterFactory factory = new LenientStringToEnumConverterFactory();
    converter = factory.getConverter(TestType.class);
  }

  @DisplayName("대소문자 구분안함, 특수문자 제거 처리후 비교")
  @ParameterizedTest
  @ValueSource(strings = {"A_TYPE", "a_type", "atype", "a$type", "a^type"})
  void convert(String source) {
    // when
    TestType result = converter.convert(source);

    // then
    assertThat(result).isSameAs(TestType.A_TYPE);
  }

  @DisplayName("Code 인터페이스 구현한 Enum 일 경우 해당 Code 값으로 변경 가능")
  @ParameterizedTest
  @EnumSource(TestType.class)
  void convert(TestType testType) {
    // given
    String code = testType.getCode();

    // when
    TestType result = converter.convert(code);

    // then
    assertThat(result).isSameAs(testType);
  }

  @DisplayName("문자열이 비어있을 경우 null 반환")
  @ParameterizedTest
  @EmptySource
  void convertEmpty(String source) {
    // when
    TestType result = converter.convert(source);

    // then
    assertThat(result).isNull();
  }

  @DisplayName("Enum 으로 변환할수 없을 경우 예외 발생")
  @Test
  void convertFail() {
    // when then
    assertThatIllegalArgumentException().isThrownBy(() -> converter.convert("invalid-source"));
  }

  @RequiredArgsConstructor
  @Getter
  enum TestType implements Code {
    A_TYPE("1"),
    B_TYPE("2");

    private final String code;
  }

}
