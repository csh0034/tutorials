package com.ask.javacore.stream;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StreamTest {

  @Test
  void groupingBy() {
    List<Rank> ranks = Arrays.asList(Rank.FIRST, Rank.FIRST, Rank.THIRD);

    Map<Rank, Long> result = ranks.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    System.out.println("result = " + result);

    Map<Rank, Long> result2 = Stream.of(Rank.values())
        .collect(toMap(Function.identity(), rank -> result.getOrDefault(rank, 0L), (a, b) -> b, LinkedHashMap::new));

    System.out.println("result2 = " + result2);
  }

  enum Rank {
    FIRST, SECOND, THIRD, FORTH
  }

}
