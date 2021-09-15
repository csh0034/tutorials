package com.ask.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = StepFailJobConfig.JOB_NAME)
public class StepFailJobConfig {

  public static final String JOB_NAME = "stepFailJob";

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean(JOB_NAME)
  public Job stepJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(simpleStep1())
        .next(simpleStep2())
        .listener(new JobExecutionListenerSupport() {
          @Override
          public void afterJob(JobExecution jobExecution) {
            if (jobExecution.getStatus() == BatchStatus.FAILED) {
              log.info(">>>>> This is after simpleStep2 : FAILED!!!!!");
            }
          }
        })
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
          log.info(">>>>> This is Step2 throw");
          throw new RuntimeException("Step2 throw");
        })
        .listener(new StepExecutionListenerSupport() {
          @Override
          public ExitStatus afterStep(StepExecution stepExecution) {
            log.info(">>>>> This is after Step2");
            return ExitStatus.COMPLETED;
          }
        })
        .build();
  }
}
