package com.ask.springbatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
class SpringBatchApplicationTests {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @BeforeEach
  void clearJobExecutions() {
    jobRepositoryTestUtils.removeJobExecutions();
  }

  @DisplayName("csv 파일 테이블 데이터 변환")
  @Test
  void batchJob() throws Exception {
    // GIVEN
    String file = "users.csv";

    JobParameters jobParameters = new JobParametersBuilder()
        .addString("file", file)
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    // WHEN
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // THEN
    assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
  }

}
