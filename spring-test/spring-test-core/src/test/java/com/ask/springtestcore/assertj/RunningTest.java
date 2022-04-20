package com.ask.springtestcore.assertj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RunningTest {

  @DisplayName("usingRecursiveComparison 사용")
  @Test
  void test4() {
    Depth1 actual = new Depth1(new Depth2(1), 11);
    Depth1 expected = new Depth1(new Depth2(1), 11);

    assertAll(
        () -> assertThat(actual).isNotEqualTo(expected),
        () -> assertThat(actual).usingRecursiveComparison().isEqualTo(expected),
        () -> assertThat(actual).usingRecursiveComparison().isNotEqualTo(new Depth1(new Depth2(1), 22)),
        () -> assertThat(actual).usingRecursiveComparison().isNotEqualTo(new Depth1(new Depth2(2), 11))
    );
  }

  static class Depth1 {

    private final Depth2 depth2;
    private final int value;

    Depth1(final Depth2 depth2, final int value) {
      this.depth2 = depth2;
      this.value = value;
    }

  }

  static class Depth2 {

    private final int value;

    Depth2(final int value) {
      this.value = value;
    }

  }

}
