package com.ask.springbatch.job;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ask.springbatch.config.TestBatchConfig;
import com.ask.springbatch.entity.User;
import com.ask.springbatch.repository.UserRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {PagingReaderJobConfig.class, TestBatchConfig.class})
@SpringBatchTest
class PagingReaderJobTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup() {
    jobRepositoryTestUtils.removeJobExecutions();

    List<User> users = IntStream.iterate(1, i -> i + 1)
        .limit(1005)
        .mapToObj(i -> User.create( "id" + i, "name" + i))
        .collect(toList());

    userRepository.deleteAll();
    userRepository.saveAll(users);
  }

  @DisplayName("JpaPagingItemReader 테스트")
  @Test
  void batchJob() throws Exception {
    // GIVEN
    JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();

    // WHEN
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // THEN
    assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
  }

}
