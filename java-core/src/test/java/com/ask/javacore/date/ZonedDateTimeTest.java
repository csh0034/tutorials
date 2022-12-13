package com.ask.javacore.date;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class ZonedDateTimeTest {

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

}
