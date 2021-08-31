package com.ask.springbatch.job;

import com.ask.springbatch.entity.User;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = JdbcCursorItemReaderJobConfig.JOB_NAME)
public class JdbcCursorItemReaderJobConfig {

  public static final String JOB_NAME = "jdbcCursorItemReaderJob";
  private static final int CHUNK_SIZE = 10;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;

  @Bean
  public Job jdbcCursorItemReaderJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .start(jdbcCursorItemReaderStep())
        .build();
  }

  @Bean
  @JobScope
  public Step jdbcCursorItemReaderStep() {
    return stepBuilderFactory.get("jdbcCursorItemReaderStep")
        .<User, User>chunk(CHUNK_SIZE)
        .reader(jdbcCursorItemReader())
        .writer(jdbcCursorItemWriter())
        .build();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<User> jdbcCursorItemReader() {
    return new JdbcCursorItemReaderBuilder<User>()
        .name("jdbcCursorItemReader")
        .fetchSize(CHUNK_SIZE)
        .dataSource(dataSource)
        .rowMapper((rs, rowNum)  -> {
          User user = User.create(
              rs.getString("name"),
              rs.getString("password"),
              rs.getBoolean("enabled")
          );
          user.setId(rs.getString("user_id"));
          return user;
        })
        .sql("SELECT user_id, name, password, enabled FROM tb_user")
        .build();
  }

  private ItemWriter<User> jdbcCursorItemWriter() {
    return users -> {
      for (User user: users) {
        log.info("Current User={}", user);
      }
    };
  }
}
