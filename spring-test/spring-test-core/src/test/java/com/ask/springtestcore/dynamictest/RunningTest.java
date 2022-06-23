package com.ask.springtestcore.dynamictest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class RunningTest {

  @TestFactory
  Stream<DynamicTest> sampleDynamicTest() {
    return Stream.of(
        dynamicTest("First Dynamic Test", () -> {
          // code...
        }),
        dynamicTest("Second Dynamic test", () -> {
          // code...
        })
    );
  }

  @DisplayName("Set 학습테스트")
  @TestFactory
  Stream<DynamicTest> setDynamicTest() {
    return Stream.of(
        dynamicTest("사이즈 검증", () -> {
          // given
          Set<String> set = new HashSet<>();

          // when
          set.add("A");
          set.add("B");
          set.add("C");

          // then
          assertThat(set).hasSize(3);
        }),
        dynamicTest("retailAll 검증", () -> {
          // given
          Set<String> set1 = new LinkedHashSet<>();
          set1.add("A");
          set1.add("B");
          set1.add("C");

          Set<String> set2 = new LinkedHashSet<>();
          set2.add("A");
          set2.add("B");

          // when
          set1.retainAll(set2);

          // then
          assertThat(set1).containsExactly("A", "B");
        })
    );
  }

}
