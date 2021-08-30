package com.ask.springbatch.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobLauncherController {

  private final JobLauncher jobLauncher;

  private final Job migrationUserJob;

  private final Job stepJob;

  @GetMapping("/job1")
  public String launchJob1(@RequestParam(value = "file", defaultValue = "users.csv") String file) {

    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addString("file", file)
          .addLong("time", System.currentTimeMillis())
          .toJobParameters();
      jobLauncher.run(migrationUserJob, jobParameters);
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    return "Complete";
  }

  @GetMapping("/job2")
  public String launchJob2() {

    try {
      JobParameters jobParameters = new JobParametersBuilder()
          .addLong("time", System.currentTimeMillis())
          .toJobParameters();
      jobLauncher.run(stepJob, jobParameters);
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    return "Complete";
  }
}
