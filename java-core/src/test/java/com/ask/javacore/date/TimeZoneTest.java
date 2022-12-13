package com.ask.javacore.date;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeZoneTest {

  /**
   * 서버가 utc 라면 그대로 utc 로 내려가고 <br>
   * kst 라면 utc 가 적용되어 -9 시간 하여 포맷팅된다.
   */
  @DisplayName("서버 타임존 상관없이 utc 로 formatting")
  @Test
  void timezone() {
    Date date = new Date();
    TimeZone utc = TimeZone.getTimeZone(ZoneOffset.UTC);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    simpleDateFormat.setTimeZone(utc);

    String result = simpleDateFormat.format(date);
    System.out.println(result);
  }

}
