package com.ask.javacore.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseTest {

  protected static void print(String message) {
    System.out.printf("(%s) [%s] %s%n", Thread.currentThread().getName(), getFormattedNow(), message);
  }

  protected static void print(int index, String message) {
    System.out.printf("(%s) [%s] index = %d %s%n", Thread.currentThread().getName(), getFormattedNow(), index, message);
  }

  private static String getFormattedNow() {
    return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
  }
}
