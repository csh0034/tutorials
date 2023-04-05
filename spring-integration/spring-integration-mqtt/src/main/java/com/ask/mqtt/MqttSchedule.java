package com.ask.mqtt;

import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class MqttSchedule {

  @Scheduled(cron = "0/10 * * * * ?")
  public void run() {
    MqttUtils.checkAlive("tcp://localhost:1883");
  }

}
