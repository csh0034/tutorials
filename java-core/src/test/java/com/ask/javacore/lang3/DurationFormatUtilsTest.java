package com.ask.javacore.lang3;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.jupiter.api.Test;

class DurationFormatUtilsTest {

  @Test
  void formatDuration() {
    Duration duration = Duration.ofSeconds(65);
    String result = DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss", true);
    assertThat(result).isEqualTo("00:01:05");
  }

}
