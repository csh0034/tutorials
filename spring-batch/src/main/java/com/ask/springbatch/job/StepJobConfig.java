package com.ask.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = StepJobConfig.JOB_NAME)
public class StepJobConfig {

  public static final String JOB_NAME = "stepJob";

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  private final StepTasklet stepTasklet;

  @Bean(JOB_NAME)
  public Job stepJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(simpleStep1())
        .next(simpleStep2())
        .next(simpleStep3())
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

  @Bean(JOB_NAME + "Step3")
  @JobScope
  public Step simpleStep3() {
    return stepBuilderFactory.get("simpleStep3")
        .tasklet(stepTasklet)
        .listener(new StepExecutionListener() {
          @Override
          public void beforeStep(StepExecution stepExecution) {
            log.info(">>>>> This is before Step3 - 1");
          }

          @Override
          public ExitStatus afterStep(StepExecution stepExecution) {
            log.info(">>>>> This is after Step3 - 1");
            return ExitStatus.COMPLETED;
          }
        })
        .build();
  }

  @Component
  public static class StepTasklet implements Tasklet, StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
      log.info(">>>>> This is before Step3 - 2");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
      log.info(">>>>> This is after Step3 - 2");
      return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
      log.info(">>>>> This is Step3");
      return RepeatStatus.FINISHED;
    }
  }
}
