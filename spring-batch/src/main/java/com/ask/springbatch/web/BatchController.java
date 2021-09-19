package com.ask.springbatch.web;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchController {

  private final JobRepository jobRepository;

  @GetMapping("/stepJob")
  public JobExecutionVO job() {

    String jobName = "stepJob";
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("time", 1631962603414L)
        .toJobParameters();

    JobExecution jobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

    return JobExecutionVO.from(jobExecution);
  }

  @Getter
  @Setter
  public static class JobExecutionVO {
    private String id;
    private String jobName;
    private String status;
    private String exitCode;
    private String exitDescription;
    private List<StepExecutionVO> steps;

    public static JobExecutionVO from(JobExecution jobExecution) {
      if (jobExecution == null) {
        throw new IllegalArgumentException("jobExecution is null");
      }

      JobInstance jobInstance = jobExecution.getJobInstance();

      JobExecutionVO vo = new JobExecutionVO();
      vo.id = String.valueOf(jobExecution.getId());
      vo.jobName = jobInstance.getJobName();
      vo.status = jobExecution.getStatus().name();
      vo.exitCode = jobExecution.getExitStatus().getExitCode();
      vo.exitDescription = jobExecution.getExitStatus().getExitDescription();
      vo.steps = StepExecutionVO.from(jobExecution.getStepExecutions());
      return vo;
    }
  }

  @Getter
  @Setter
  public static class StepExecutionVO {
    private String id;
    private String stepName;
    private String status;
    private String exitCode;
    private String exitDescription;
    private Date startTime;
    private Date endTime;
    private Date lastUpdated;

    public static List<StepExecutionVO> from(Collection<StepExecution> stepExecutions) {
      return stepExecutions.stream()
          .map(stepExecution -> {
            StepExecutionVO vo = new StepExecutionVO();
            vo.id = String.valueOf(stepExecution.getId());
            vo.stepName = stepExecution.getStepName();
            vo.status = stepExecution.getStatus().name();
            vo.exitCode = stepExecution.getExitStatus().getExitCode();
            vo.exitDescription = stepExecution.getExitStatus().getExitDescription();
            vo.startTime = stepExecution.getStartTime();
            vo.endTime = stepExecution.getEndTime();
            vo.lastUpdated = stepExecution.getLastUpdated();
            return vo;
          })
          .collect(toList());
    }
  }
}
