package com.ask.springbatch.job;

import com.ask.springbatch.entity.User;
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
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = JpaTransactionJobConfig.JOB_NAME)
public class JpaTransactionJobConfig {

  public static final String JOB_NAME = "jpaTransactionJob";
  private static final int CHUNK_SIZE = 100;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;

  @Bean(JOB_NAME)
  public Job jpaTransactionJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .preventRestart()
        .start(jpaTransactionStep())
        .build();
  }

  @Bean(JOB_NAME + "PagingReaderJob")
  @JobScope
  public Step jpaTransactionStep() {
    return stepBuilderFactory.get("jpaTransactionStep")
        .<User, User>chunk(CHUNK_SIZE)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
  }

  @Bean(JOB_NAME + "Reader")
  @StepScope
  public JpaPagingItemReader<User> reader() {
    return new JpaPagingItemReaderBuilder<User>()
        .name("jpaTransactionReader")
        .entityManagerFactory(entityManagerFactory)
        .pageSize(CHUNK_SIZE)
        .queryString("SELECT u FROM User u")
        .build();
  }

  @Bean(JOB_NAME + "Processor")
  public ItemProcessor<User, User> processor() {
    return user -> {
      //log.info("Lazy Loading test : {}", user.getUserExtra());
      user.disable();
      return user;
    };
  }

  // 트랜잭션이 chunk 단위 이므로 User Entity 는 detach 상태가 아니지만
  // 내부에서 merge 를 호출하므로 id가 세팅 되어 있을 경우 select, insert 발생함
  @Bean(JOB_NAME + "Writer")
  @StepScope
  public JpaItemWriter<User> writer() {
    return new JpaItemWriterBuilder<User>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }

//  public ItemWriter<User> writer() {
//    return items -> items.forEach(user -> log.info("Lazy Loading test : {}", user.getUserExtra()));
//  }
}
