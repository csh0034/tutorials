package com.ask.javacore.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import org.junit.jupiter.api.Test;

class LocalDateTimeTest {

  @Test
  void toInstant() {
    LocalDateTime localDateTime = LocalDateTime.now();
    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    System.out.println(instant);
    System.out.println(instant.toEpochMilli());
  }

  @Test
  void ofInstant() {
    Instant twitterEpoch = Instant.ofEpochMilli(1288834974657L);
    LocalDateTime localDateTime = LocalDateTime.ofInstant(twitterEpoch, ZoneOffset.UTC);
    System.out.println(localDateTime);

    Date date = Date.from(twitterEpoch);
    System.out.println(date);
  }

}
