package com.ask.springbatch.ItemWriter;

import static java.util.stream.Collectors.toList;

import com.ask.springbatch.entity.User;
import java.util.List;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JdbcBatchItemWriterTests {

  private static final int USER_SIZE = 1000;

  @Autowired
  private DataSource dataSource;

  @DisplayName("JdbcBatch 사용")
  @Test
  void jpaItemWriter() throws Exception {
    // GIVEN
    JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriterBuilder<User>()
        .dataSource(dataSource)
        .sql("insert into tb_user(user_id, name, password, enabled) values (:id, :name, :password, :enabled)")
        .beanMapped()
        .build();

    writer.afterPropertiesSet();

    List<User> items = IntStream.iterate(1, i -> i + 1)
        .limit(USER_SIZE)
        .mapToObj(i -> User.create( "id" + i, "name" + i))
        .collect(toList());

    // WHEN
    writer.write(items);
  }
}
