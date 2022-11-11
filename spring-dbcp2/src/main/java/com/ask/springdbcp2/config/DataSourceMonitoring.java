package com.ask.springdbcp2.config;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSourceMonitoring {

  private final BasicDataSource basicDataSource;

  @Scheduled(cron = "*/10 * * * * *")
  public void log() {
    log.info("{}", DataSourceInfo.from(basicDataSource));
  }


  @ToString
  private static class DataSourceInfo {

    private int total;
    private int numActive;
    private int numIdle;
    private int maxTotal;
    private int initialSize;
    private int maxIdle;
    private int minIdle;
    private long timeBetweenEvictionRunsMillis;

    private static DataSourceInfo from(BasicDataSource dataSource) {
      DataSourceInfo info = new DataSourceInfo();
      info.numActive = dataSource.getNumActive();
      info.numIdle = dataSource.getNumIdle();
      info.total = info.numActive + info.numIdle;
      info.maxTotal = dataSource.getMaxTotal();
      info.maxIdle = dataSource.getMaxIdle();
      info.minIdle = dataSource.getMinIdle();
      info.initialSize = dataSource.getInitialSize();
      info.timeBetweenEvictionRunsMillis = dataSource.getTimeBetweenEvictionRunsMillis();
      return info;
    }

  }

}
