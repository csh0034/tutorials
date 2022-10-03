package com.ask.springcore.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.ask.springcore.config.converter.LenientStringToEnumConverterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.convert.converter.Converter;

public class LenientStringToEnumConverterFactoryTest {

  private LenientStringToEnumConverterFactory factory;

  @BeforeEach
  void setUp() {
    factory = new LenientStringToEnumConverterFactory();
  }

  @DisplayName("대소문자 구분안함, 특수문자 제거 처리후 비교")
  @ParameterizedTest
  @ValueSource(strings = {"A_GOOGLE", "agoogle", "a_google", "a$google", "a^google"})
  void convert(String source) {
    // given
    Converter<String, Company> converter = factory.getConverter(Company.class);

    // when
    Company company = converter.convert(source);

    // then
    assertThat(company).isEqualTo(Company.A_GOOGLE);
  }

  @DisplayName("Code 인터페이스 상속한 Enum 일 경우 해당 Code 값으로 변경 가능")
  @Test
  void convert() {
    // given
    Converter<String, Company> converter = factory.getConverter(Company.class);
    String source = "1";

    // when
    Company company = converter.convert(source);

    // then
    assertThat(company).isEqualTo(Company.A_GOOGLE);
  }

  /**
   * null 일 경우엔 여기 까지 들어오지 않음
   */
  @DisplayName("문자열이 비어있을 경우 null 반환")
  @ParameterizedTest
  @EmptySource
  void convertEmpty(String source) {
    // given
    Converter<String, Company> converter = factory.getConverter(Company.class);

    // when
    Company company = converter.convert(source);

    // then
    assertThat(company).isNull();
  }

  @DisplayName("Enum 으로 변환할수 없을 경우 예외 발생")
  @Test
  void convertFail() {
    // given
    Converter<String, Company> converter = factory.getConverter(Company.class);

    // when then
    assertThatIllegalArgumentException().isThrownBy(() -> converter.convert("invalid-source"));
  }

}
