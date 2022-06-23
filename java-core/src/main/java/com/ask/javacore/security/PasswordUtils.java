package com.ask.javacore.security;

import java.util.regex.Pattern;
import org.apache.commons.lang3.CharUtils;

public final class PasswordUtils {

  private static final int REPETITION_LIMIT = 4;
  private static final int CONSECUTIVE_CHARACTER_LIMIT = 4;

  private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*\\d).{8,24}$");
  private static final Pattern REPETITION_PATTERN = Pattern.compile(String.format("([a-zA-Z\\d])\\1{%d}", REPETITION_LIMIT - 1));

  private PasswordUtils() {
  }

  public static boolean isValid(String rawPassword) {
    return matchesPasswordPattern(rawPassword)
        && !containsRepeatedCharacters(rawPassword)
        && !containsConsecutiveCharacters(rawPassword);
  }

  static boolean matchesPasswordPattern(String rawPassword) {
    return PASSWORD_PATTERN.matcher(rawPassword).matches();
  }

  static boolean containsRepeatedCharacters(String rawPassword) {
    return REPETITION_PATTERN.matcher(rawPassword).find();
  }

  static boolean containsConsecutiveCharacters(String rawPassword) {
    int o = 0;
    int d = 0;
    int p = 0;
    int n = 0;
    for (int i = 0; i < rawPassword.length(); i++) {
      char temp = rawPassword.charAt(i);
      if (i > 0 && (p = o - temp) > -2 && (n = p == d ? n + 1 : 0) > CONSECUTIVE_CHARACTER_LIMIT - 3) {
        if (CharUtils.isAsciiAlphanumeric(temp)) {
          return true;
        }
      }
      d = p;
      o = temp;
    }
    return false;
  }

}
