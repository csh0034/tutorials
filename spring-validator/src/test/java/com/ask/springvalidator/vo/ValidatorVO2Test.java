package com.ask.springvalidator.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ValidatorVO2Test {

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private static final String VALID_PHONE = "010-2222-3333";

  @DisplayName("ValidatorVO2 유효성 검증")
  @ParameterizedTest(name = "{index}. {2}")
  @MethodSource("provider")
  void validation(ValidatorVO2 validatorVO2, int violationsSize, String m) {
    // when
    Set<ConstraintViolation<ValidatorVO2>> constraintViolations = validator.validate(validatorVO2);

    // then
    assertThat(constraintViolations.size()).isEqualTo(violationsSize);
  }

  static Stream<Arguments> provider() {
    return Stream.of(
        arguments(new ValidatorVO2(VALID_PHONE), 0, "정상"),
        arguments(new ValidatorVO2(""), 1, "빈칸"),
        arguments(new ValidatorVO2("phone"), 1, "문자열"),
        arguments(new ValidatorVO2("021-phone-4059"), 1, "숫자, 문자 혼용")
    );
  }
}