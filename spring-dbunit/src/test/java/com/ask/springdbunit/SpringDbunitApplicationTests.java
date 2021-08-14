package com.ask.springdbunit;

import java.io.InputStream;
import java.sql.Connection;
import javax.sql.DataSource;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class SpringDbunitApplicationTests {

  @Autowired
  private DataSource dataSource;

  @Test
  void dbunit() throws Exception {
    Connection connection = dataSource.getConnection();
    MySqlConnection iDatabaseConnection = new MySqlConnection(connection, "dbunit");

    InputStream is = new ClassPathResource("data.xml").getInputStream();
    FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder().build(is);
    DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, flatXmlDataSet);
  }

}
