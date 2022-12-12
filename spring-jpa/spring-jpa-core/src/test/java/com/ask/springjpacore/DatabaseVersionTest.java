package com.ask.springjpacore;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

@SpringBootTest
@Slf4j
class DatabaseVersionTest {

  @Autowired
  private DataSource dataSource;

  @Test
  void version() throws MetaDataAccessException {
    DatabaseMetaDataDto databaseMetaDataDto = JdbcUtils.extractDatabaseMetaData(dataSource, databaseMetaData ->
        DatabaseMetaDataDto.of(databaseMetaData.getDatabaseProductName(), databaseMetaData.getDatabaseProductVersion(),
            databaseMetaData.getDatabaseMajorVersion(), databaseMetaData.getDatabaseMinorVersion()));

    log.info("databaseMetaDataDto: {}", databaseMetaDataDto);
  }

  @RequiredArgsConstructor(staticName = "of")
  @ToString
  private static class DatabaseMetaDataDto {

    private final String productName;
    private final String productVersion;
    private final int majorVersion;
    private final int minorVersion;

  }

}
