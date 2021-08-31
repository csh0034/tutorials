package com.ask.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = StepJobConfig.JOB_NAME)
public class StepJobConfig {

  public static final String JOB_NAME = "stepJob";

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean(JOB_NAME)
  public Job stepJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(simpleStep1())
        .next(simpleStep2())
        .build();
  }

  @Bean(JOB_NAME + "Step1")
  @JobScope
  public Step simpleStep1() {
    return stepBuilderFactory.get("simpleStep1")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>> This is Step1");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean(JOB_NAME + "Step2")
  @JobScope
  public Step simpleStep2() {
    return stepBuilderFactory.get("simpleStep2")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>> This is Step2");
          return RepeatStatus.FINISHED;
        })
        .build();
  }
}
