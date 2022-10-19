package com.ask.javacore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.javacore.common.BaseTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BigDecimalTest extends BaseTest {

  @DisplayName("기본값 검증")
  @Test
  void assertion1() {
    BigDecimal bigDecimal = new BigDecimal("123.450");

    assertAll(
        () -> assertThat(bigDecimal.unscaledValue()).isEqualTo(123450),
        () -> assertThat(bigDecimal.scale()).isSameAs(3),
        () -> assertThat(bigDecimal.precision()).isSameAs(6)
    );
  }

  @DisplayName("equals 의 경우 value 와 scale 을 비교한다.")
  @Test
  void assertion2() {
    assertThat(new BigDecimal("2.1").equals(new BigDecimal("2.10"))).isFalse();
  }

  @DisplayName("compareTo 의 경우 scale 은 비교하지 않고 value 만 비교한다.")
  @Test
  void assertion3() {
    assertThat(new BigDecimal("2.1").compareTo(new BigDecimal("2.10"))).isSameAs(0);
  }

}
