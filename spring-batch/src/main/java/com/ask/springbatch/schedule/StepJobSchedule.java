package com.ask.springbatch.schedule;

import com.ask.springbatch.job.StepJobConfig;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@ConditionalOnProperty(name = "job.name", havingValue = StepJobConfig.JOB_NAME)
@Component
@RequiredArgsConstructor
@Slf4j
public class StepJobSchedule {

  @Resource(name = StepJobConfig.JOB_NAME)
  private Job job;

  private final JobLauncher jobLauncher;

  @Scheduled(fixedRate = 5000, initialDelay = 5000)
  public void stepJobSchedule() throws Exception {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    log.info("stepJobSchedule start");

    jobLauncher.run(job, getJobParameters());

    stopWatch.stop();
    log.info("stepJobSchedule stop : {}", stopWatch.getTotalTimeMillis());
  }

  private JobParameters getJobParameters() {
    return new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
  }
}
