package com.ask.springjpaquerydsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class ClassifyMapTest {

  @DisplayName("오라클에선 group by 와 select 절이 같아야하므로 group by 처리하지 않고 모두 가져와 map 을 통해 처리")
  @Test
  void classify2() {
    List<RecentContactVO> list = Arrays.asList(
        new RecentContactVO("user01", "a"),
        new RecentContactVO("user02", "b"),
        new RecentContactVO("user01", "c")
    );

    Map<String, RecentContactVO> map = list.stream()
        .collect(Collectors.toMap(RecentContactVO::getUserId, Function.identity(), (p1, p2) -> p1, LinkedHashMap::new));

    List<RecentContactVO> values = new ArrayList<>(map.values());
    log.info("values: {}", values);
  }

  @AllArgsConstructor
  @Getter
  @ToString
  private static class RecentContactVO {

    private String userId;
    private String displayName;

  }

}
