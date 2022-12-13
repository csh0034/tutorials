package com.ask.javacore.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class LocalDateTimeTest {

  @Test
  void toInstant() {
    LocalDateTime localDateTime = LocalDateTime.now();
    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    System.out.println(instant);
    System.out.println(instant.toEpochMilli());
  }

}
