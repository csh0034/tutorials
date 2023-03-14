package com.ask.springcore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @see <a href="https://www.baeldung.com/spring-jdbc-jdbctemplate">baeldung Spring JDBC</a>
 */
@SpringBootTest
class NamedParameterJdbcTemplateTest {

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private Properties sqlProperties;

  @Test
  void sql() {
    String sql = sqlProperties.getProperty("select-name");

    Map<String, String> params = new HashMap<>();
    params.put("name", "ask");

    String name = namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> rs.getString("name"));

    assertThat(name).isEqualTo("ask");
  }

}
