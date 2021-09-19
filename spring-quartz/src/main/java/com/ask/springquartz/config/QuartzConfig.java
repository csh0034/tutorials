package com.ask.springquartz.config;

import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

  private static final String JOB_NAME = "printTimeJob";

  private final Scheduler scheduler;

  @PostConstruct
  public void init() throws Exception {

    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("test", "key");

    JobDetail jobDetail = JobBuilder
        .newJob(PrintTimeCronJob.class)
        .withIdentity(JOB_NAME)
        .withDescription("10초에 한번씩 시간을 출력하는 Job")
        .usingJobData(jobDataMap)
        .build();

    CronTrigger trigger = TriggerBuilder.newTrigger()
        .withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * * * ?"))
        .build();

    if (!scheduler.checkExists(new JobKey(JOB_NAME))) {
      scheduler.scheduleJob(jobDetail, trigger);
    }
  }

  @Component
  @Slf4j
  public static class PrintTimeCronJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
      JobKey jobKey = context.getJobDetail().getKey();

      log.info("jobKey name : {}, now : {}", jobKey.getName(), LocalDateTime.now());
    }
  }
}
