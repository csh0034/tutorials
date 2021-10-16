package com.ask.springvalidator.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class ValidatorVO1Test {

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private static final String VALID_NAME = "name";
  private static final int VALID_AGE = 15;

  @DisplayName("ValidatorVO1 유효성 검증")
  @ParameterizedTest(name = "{index}. {2}")
  @MethodSource("provider")
  void validation(ValidatorVO1 validatorVO1, int violationsSize, String m) {
    // when
    Set<ConstraintViolation<ValidatorVO1>> constraintViolations = validator.validate(validatorVO1);

    // then
    assertThat(constraintViolations.size()).isEqualTo(violationsSize);

    constraintViolations.forEach(violation -> log.info("{} : {}", violation.getPropertyPath(), violation.getMessage()));
  }

  static Stream<Arguments> provider() {
    return Stream.of(
        arguments(new ValidatorVO1(VALID_NAME, VALID_AGE), 0, "정상"),
        arguments(new ValidatorVO1("", VALID_AGE), 1, "name 빈값"),
        arguments(new ValidatorVO1("nam@", VALID_AGE), 1, "name 특수문자 포함"),
        arguments(new ValidatorVO1("", 5), 2, "name 빈값, age 10 미만"),
        arguments(new ValidatorVO1("", 40), 2, "name 빈값, age 30 초과")
    );
  }
}