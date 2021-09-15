package com.ask.springbatch.job;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@SpringBatchTest
@TestPropertySource(properties = {"job.name=" + StepFailJobConfig.JOB_NAME})
class StepFailJobTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @BeforeEach
  void clearJobExecutions() {
    jobRepositoryTestUtils.removeJobExecutions();
  }

  @DisplayName("스텝 테스트")
  @Test
  void batchJob() throws Exception {
    // GIVEN
    JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();

    // WHEN
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // THEN
    assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
  }

}
