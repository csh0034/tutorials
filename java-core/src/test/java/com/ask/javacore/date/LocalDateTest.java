package com.ask.javacore.date;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocalDateTest {

  @DisplayName("윤년 계산")
  @Test
  void leapYear() {
    LocalDate localDate = LocalDate.of(2020, 2, 1);
    System.out.println("localDate.isLeapYear() = " + localDate.isLeapYear());

    assertThat(localDate.isLeapYear()).isTrue();
  }

}
