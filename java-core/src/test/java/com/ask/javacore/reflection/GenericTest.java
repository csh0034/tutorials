package com.ask.javacore.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GenericTest {

  @DisplayName("superclass 의 generic type 가져오기")
  @Test
  void reflect() throws Exception {
    Class<?> clazz = ConcreteGenericSample.class;

    ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
    for (Type type : parameterizedType.getActualTypeArguments()) {
      System.out.println("type = " + type);
    }

    Class<?> string = (Class<?>) parameterizedType.getActualTypeArguments()[0];
    String value = (String) string.getDeclaredConstructor(String.class).newInstance("TEST");

    assert value.equals("TEST");

    Class<?> exception = (Class<?>) parameterizedType.getActualTypeArguments()[1];
    RuntimeException runtimeException = (RuntimeException) exception.getDeclaredConstructor(String.class)
        .newInstance("message...");

    assert runtimeException.getMessage().equals("message...");
  }


  public static abstract class GenericSample<T, U> {

    public T t;
    public U u;
  }

  public static class ConcreteGenericSample extends GenericSample<String, RuntimeException> {

  }

}
