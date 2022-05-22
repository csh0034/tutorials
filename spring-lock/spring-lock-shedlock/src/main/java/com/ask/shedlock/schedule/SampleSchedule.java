package com.ask.shedlock.schedule;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleSchedule {

  @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
  @SchedulerLock(name = "sampleSchedule", lockAtLeastFor = "9s", lockAtMostFor = "9s")
  public void scheduledTask() throws InterruptedException {
    log.info("sampleSchedule start....");
    Thread.sleep(5000);
    log.info("sampleSchedule done....");
  }

}
