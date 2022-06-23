package com.ask.javacore.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordUtilsTest {

  @ParameterizedTest
  @MethodSource("isValidArguments")
  void isValid(String password, boolean expected) {
    assertThat(PasswordUtils.isValid(password)).isEqualTo(expected);
  }

  @ParameterizedTest
  @ValueSource(strings = {"vzcRpsPQ4@", "NL3uu9!Ak7bTI", "ELGUuh&^N2Wx", "c6@#Z4jep"})
  void matchesPasswordPattern(String password) {
    assertThat(PasswordUtils.matchesPasswordPattern(password)).isTrue();
  }

  @ParameterizedTest
  @MethodSource("containsRepeatedCharactersArguments")
  void containsRepeatedCharacters(String password, boolean expected) {
    assertThat(PasswordUtils.containsRepeatedCharacters(password)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("consecutiveCharactersArguments")
  void containsConsecutiveCharacters(String password, boolean expected) {
    assertThat(PasswordUtils.containsConsecutiveCharacters(password)).isEqualTo(expected);
  }

  static Stream<Arguments> isValidArguments() {
    return Stream.of(
        arguments("apple123@", true),
        arguments("1234abc@", false),
        arguments("1111abc@", false),
        arguments("abcd123@", false),
        arguments("aaaa123@", false)
    );
  }

  static Stream<Arguments> containsRepeatedCharactersArguments() {
    return Stream.of(
        arguments("1111", true),
        arguments("2222", true),
        arguments("11112222", true),
        arguments("aaaa", true),
        arguments("bbbb", true),
        arguments("aaaabbbb", true),
        arguments("@@@@", false),
        arguments("!!!!", false)
    );
  }

  static Stream<Arguments> consecutiveCharactersArguments() {
    return Stream.of(
        arguments("1234", true),
        arguments("2345", true),
        arguments("4321", true),
        arguments("abcd", true),
        arguments("ABCD", true),
        arguments("bcde", true),
        arguments("dcba", true),
        arguments("DCBA", true),
        arguments("#$%&", false),
        arguments("&%$#", false)
    );
  }

}
