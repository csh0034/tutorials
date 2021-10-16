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

class ValidatorVO3Test {

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @DisplayName("ValidatorVO3 유효성 검증")
  @ParameterizedTest
  @MethodSource("provider")
  void validation(ValidatorVO3 validatorVO3, int violationsSize) {
    // when
    Set<ConstraintViolation<ValidatorVO3>> constraintViolations = validator.validate(validatorVO3);

    // then
    assertThat(constraintViolations.size()).isEqualTo(violationsSize);
  }

  static Stream<Arguments> provider() {
    return Stream.of(
        arguments(new ValidatorVO3(null, null), 1),
        arguments(new ValidatorVO3(true, false), 0),
        arguments(new ValidatorVO3(true, true), 1),
        arguments(new ValidatorVO3(false, false), 1),
        arguments(new ValidatorVO3(false, true), 2)
    );
  }
}