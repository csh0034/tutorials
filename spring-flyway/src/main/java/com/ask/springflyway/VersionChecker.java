package com.ask.springflyway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class VersionChecker implements ApplicationRunner {

  private final DataSourceProperties dataSourceProperties;

  @Override
  public void run(ApplicationArguments args) {
    log.info("flyway script 경로의 제일 최신버전 출력: {}", baselineVersion());
  }

  private String baselineVersion() {
    Flyway flyway = Flyway.configure()
        .dataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
        .locations("classpath:db/migration")
        .load();
    MigrationInfoService migrationInfoService = flyway.info();
    MigrationInfo[] migrationInfos = migrationInfoService.all();
    if (migrationInfos == null || migrationInfos.length == 0) {
      return "1";
    }

    return migrationInfos[migrationInfos.length - 1].getVersion().getVersion();
  }

}
