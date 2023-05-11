package com.ask.javacore.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class StreamTest {

  @Test
  void groupingBy() {
    List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3, 10);

    Map<Integer, Long> result = list.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    System.out.println("result = " + result);
  }

}
