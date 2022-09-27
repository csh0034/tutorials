package com.ask.javacore.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReflectionUtilsTest {

  @DisplayName("target 의 제네릭 타입 추출")
  @ParameterizedTest
  @MethodSource("extractFirstGenericTypeArguments")
  void extractFirstGenericType(Class<?> target, Class<?> expected) {
    Class<?> clazz = ReflectionUtils.extractFirstGenericType(target);
    assertThat(clazz).isSameAs(expected);
  }

  static Stream<Arguments> extractFirstGenericTypeArguments() {
    return Stream.of(
        arguments(AClass.class, String.class),
        arguments(BClass.class, Integer.class)
    );
  }

  private abstract static class Generic<T> {
  }

  private static class AClass extends Generic<String> {
  }

  private static class BClass extends Generic<Integer> {
  }

}
