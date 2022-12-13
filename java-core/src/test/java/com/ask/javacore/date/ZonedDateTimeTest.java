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
  void zone() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    System.out.println(zonedDateTime);
  }

}
