package com.ask.springbatch.job;

import com.ask.springbatch.entity.User;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PagingReaderJobConfig {

  public static final String JOB_NAME = "pagingReaderJob";
  private static final int CHUNK_SIZE = 100;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;

  @Bean(JOB_NAME)
  public Job pagingReaderJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .preventRestart()
        .start(pagingReaderStep())
        .build();
  }

  @Bean(JOB_NAME + "PagingReaderJob")
  @JobScope
  public Step pagingReaderStep() {
    return stepBuilderFactory.get("migrationUserStep")
        .<User, User>chunk(CHUNK_SIZE)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
  }

  @Bean(JOB_NAME + "Reader")
  @StepScope
  public JpaPagingItemReader<User> reader() {
    String jpql = "select u from User u where u.enabled = :enabled";

    Map<String, Object> map = new HashMap<>();
    map.put("enabled", true);

    // 처음 이후의 처리에 대해 데이터 건너뛰기가 발생 하기 때문에 매번 첫 번째 페이지만 참조할 수 있도록 변경
    JpaPagingItemReader<User> reader = new JpaPagingItemReader<User>() {
      @Override
      public int getPage() {
        return 0;
      }
    };

    reader.setQueryString(jpql);
    reader.setParameterValues(map);
    reader.setPageSize(CHUNK_SIZE);
    reader.setEntityManagerFactory(entityManagerFactory);

    return reader;
  }

  @Bean(JOB_NAME + "Processor")
  public ItemProcessor<User, User> processor() {
    return user -> {
      user.disable();
      return user;
    };
  }

  // Processor 넘어온 User Entity 는 DETACH 상태이므로 merge 시에 select, insert 발생함
  @Bean(JOB_NAME + "Writer")
  @StepScope
  public JpaItemWriter<User> writer() {
    return new JpaItemWriterBuilder<User>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }
}
