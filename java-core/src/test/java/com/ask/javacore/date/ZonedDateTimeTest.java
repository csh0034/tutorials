package com.ask.javacore.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class ZonedDateTimeTest {

  @Test
  void convert() {
    ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
    System.out.println(zonedDateTime);
  }

  @Test
  void getAvailableZoneIds() {
    ZoneId.getAvailableZoneIds().forEach(System.out::println);
  }

  @Test
  void zoneId() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    ZoneId zoneId = zonedDateTime.getZone();
    System.out.println(zoneId);
  }

  @Test
  void withZoneSameInstant() {
    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
    System.out.println(utc);

    ZonedDateTime kst = utc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
    System.out.println(kst);
  }

  @Test
  void withFormat() {
    ZonedDateTime zonedDateTime = Instant.ofEpochSecond(1679287861).atZone(ZoneId.of("Asia/Seoul"));
    System.out.println(zonedDateTime);

    String formatted = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    System.out.println(formatted);
  }

}
