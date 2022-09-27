package com.ask.javacore.util;

import java.lang.reflect.ParameterizedType;

public final class ReflectionUtils {

  private ReflectionUtils() {
  }

  public static Class<?> extractFirstGenericType(Class<?> target) {
    return extractGenericType(target, 0);
  }

  public static Class<?> extractGenericType(Class<?> target, int index) {
    return (Class<?>) ((ParameterizedType) target.getGenericSuperclass()).getActualTypeArguments()[index];
  }

}
