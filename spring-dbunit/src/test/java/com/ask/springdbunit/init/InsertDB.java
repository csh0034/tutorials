package com.ask.springdbunit.init;

import java.io.InputStream;
import java.sql.Connection;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = "spring.jpa.hibernate.ddl-auto=update")
@Slf4j
class InsertDB {

  @Autowired
  DataSource dataSource;

  @BeforeAll
  static void beforeAll() {
    if (System.getProperty("db.name") == null) {
      throw new IllegalStateException("system property 'db.name' must not be null");
    };
  }

  @DisplayName("dbunit 사용 데이터 초기화")
  @Test
  void dbunit() throws Exception {

    String dbName = System.getProperty("db.name");

    Connection connection = dataSource.getConnection();
    MySqlConnection iDatabaseConnection = new MySqlConnection(connection, dbName);

    InputStream is = new ClassPathResource("data.xml").getInputStream();
    FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder().build(is);
    DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, flatXmlDataSet);

    connection.close();
    iDatabaseConnection.close();
  }
}
