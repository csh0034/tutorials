package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QStatistics.statistics;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@Slf4j
class StatisticsRepositoryTest {

  @Autowired
  private JPAQueryFactory queryFactory;

  @DisplayName("group by 시에 concat 사용")
  @Test
  void groupBy() {
    // when
    StringExpression groupByTarget = statistics.year.concat("-").concat(statistics.month);

    List<StatisticsDto> result = queryFactory.select(Projections.constructor(StatisticsDto.class,
            groupByTarget,
            statistics.viewCount.sum()))
        .from(statistics)
        .groupBy(groupByTarget)
        .fetch();

    // then
    log.info("result : {}", result);
    assertAll(
        () -> assertThat(result.get(0).date).isEqualTo("2021-12"),
        () -> assertThat(result.get(0).sum).isEqualTo(28)
    );
  }

  @AllArgsConstructor
  @ToString
  public static class StatisticsDto {

    private String date;
    private Integer sum;
  }
}
