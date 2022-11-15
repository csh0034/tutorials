package com.ask.springbatch.check;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BatchMetaTableRepositoryTest {

  private static final String FIXED_TARGET_BATCH_NAME = "SampleBatch";

  @Autowired
  private BatchMetaTableRepository batchMetaTableRepository;

  @DisplayName("실패한 배치 count 를 조회한다.")
  @Test
  void countFailedBatch() {
    // given
    Duration duration = Duration.ofDays(1);

    // when
    long count = batchMetaTableRepository.countFailedBatch(FIXED_TARGET_BATCH_NAME, duration);

    // then
    assertThat(count).isGreaterThanOrEqualTo(0);
  }

  @DisplayName("성공한 배치 count 를 조회한다.")
  @Test
  void countCompletedBatch() {
    // given
    Duration duration = Duration.ofDays(1);

    // when
    long count = batchMetaTableRepository.countCompletedBatch(FIXED_TARGET_BATCH_NAME, duration);

    // then
    assertThat(count).isGreaterThanOrEqualTo(0);
  }

  @DisplayName("등록되지 않은 배치의 경우 가장 최근 jobExecutionId 조회시 null 을 반환한다.")
  @Test
  void findMaxJobExecutionIdByJobName() {
    // given
    String unregisteredBatchName = "unregisteredBatchName";

    // when
    Long jobExecutionId = batchMetaTableRepository.findMaxJobExecutionIdByJobName(unregisteredBatchName);

    // then
    assertThat(jobExecutionId).isNull();
  }

  @DisplayName("존재하지 않는 executionId 의 경우 현재 상태 조회시 null 을 반환한다.")
  @Test
  void findStatusByJobExecutionId() {
    // given
    Long nonexistentExecutionId = -1L;

    // when
    String status = batchMetaTableRepository.findStatusByJobExecutionId(nonexistentExecutionId);

    // then
    assertThat(status).isNull();
  }

}
