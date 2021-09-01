package com.ask.springbatch.job;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ask.springbatch.entity.User;
import com.ask.springbatch.entity.UserExtra;
import com.ask.springbatch.repository.UserExtraRepository;
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
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@SpringBatchTest
@TestPropertySource(properties = {"job.name=" + JpaTransactionJobConfig.JOB_NAME})
class JpaTransactionJobTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserExtraRepository userExtraRepository;

  @BeforeEach
  void setup() {
    System.out.println(">>>>>>>>>>>>>> setup start <<<<<<<<<<<<<<<<");
    jobRepositoryTestUtils.removeJobExecutions();

    userRepository.deleteAll();
    userExtraRepository.deleteAll();

    List<User> users = IntStream.iterate(1, i -> i + 1)
        .limit(15)
        .mapToObj(i -> User.create("name" + i, "password" + i, true))
        .collect(toList());

    userRepository.saveAll(users);
    users.stream()
        .filter(user -> user.getId().contains("1"))
        .forEach(user -> userExtraRepository.save(UserExtra.create(user)));

    System.out.println(">>>>>>>>>>>>>> setup end <<<<<<<<<<<<<<<<");
  }

  @DisplayName("JpaTransaction 테스트")
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
