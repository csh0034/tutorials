package com.ask.springcore.constants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.Constants;

class ConstantsTest {

  private static final Constants constants = new Constants(CloseType.class);

  @Test
  void asNumber() {
    assertAll(
        () -> assertThat(constants.asNumber("ABNORMAL").intValue()).isEqualTo(1),
        () -> assertThat(constants.asNumber("NORMAL").intValue()).isEqualTo(2),
        () -> assertThat(constants.asNumber("BATCH").intValue()).isEqualTo(3)
    );
  }

  @Test
  void toCode() {
    assertAll(
        () -> assertThat(constants.toCode(1, null)).isEqualTo("ABNORMAL"),
        () -> assertThat(constants.toCode(2, null)).isEqualTo("NORMAL"),
        () -> assertThat(constants.toCode(3, null)).isEqualTo("BATCH")
    );
  }

  @DisplayName("HashSet 을 사용하므로 선언 순서 보장 안됨")
  @Test
  void getNames() {
    Set<String> names = constants.getNames(null);
    assertThat(names).contains("ABNORMAL", "NORMAL", "BATCH");
  }

  @Test
  void getValues() {
    Set<Object> values = constants.getValues(null);
    assertThat(values).contains(1, 2, 3);
  }

}
