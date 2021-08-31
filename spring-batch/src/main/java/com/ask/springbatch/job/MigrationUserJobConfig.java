package com.ask.springbatch.job;

import com.ask.springbatch.entity.User;
import com.ask.springbatch.repository.UserRepository;
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
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = MigrationUserJobConfig.JOB_NAME)
public class MigrationUserJobConfig {

  public static final String JOB_NAME = "migrationUserJob";
  private static final int CHUNK_SIZE = 100;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;
  private final UserRepository userRepository;

  private final MigrationUserJobParameter jobParameter;

  @Bean(JOB_NAME + "Parameter")
  @JobScope
  public MigrationUserJobParameter jobParameter() {
    return new MigrationUserJobParameter();
  }

  @Bean(JOB_NAME)
  public Job migrationUserJob() {
    return jobBuilderFactory.get(JOB_NAME)
        .preventRestart()
        .start(deleteUserStep())
        .next(migrationUserStep())
        .build();
  }

  @Bean(JOB_NAME + "DeleteUserStep")
  @JobScope
  public Step deleteUserStep() {
    return stepBuilderFactory.get("deleteUserStep")
        .tasklet((contribution, chunkContext) -> {
          userRepository.deleteAllInBatch();
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean(JOB_NAME + "MigrationUserStep")
  @JobScope
  public Step migrationUserStep() {
    return stepBuilderFactory.get("migrationUserStep")
        .<User, User>chunk(CHUNK_SIZE)
        .reader(migrationUserReader())
        .faultTolerant()
        .skipPolicy((t, skipCount) -> {
          log.info("skip({}) : {}", (skipCount + 1), t.getMessage());
          return true;
        })
        .processor(migrationUserProcessor())
        .writer(migrationUserWriter())
        .build();
  }

  @Bean(JOB_NAME + "Reader")
  @StepScope
  public FlatFileItemReader<User> migrationUserReader() {

    log.info(">>>>>>>>>>>>>> MigrationUserJobParameter : {}", jobParameter);

    return new FlatFileItemReaderBuilder<User>()
        .name("migrationUserReader")
        .resource(new ClassPathResource(jobParameter.getFile()))
        .lineMapper(defaultLineMapper())
        .build();
  }

  // @StepScope 는 기본 프록시 모드가 반환되는 클래스 타임을 참조하기 때문에 @StepScope 를 사용하면 반드시 구현된 반환 타입을 명시해 변환해야한다.
  @Bean(JOB_NAME + "Processor")
  public ItemProcessor<User, User> migrationUserProcessor() {
    return user -> {
      if (!user.isEnabled()) {
        log.info("disabled : {}", user);
        return null;
      }
      return user;
    };
  }

  @Bean(JOB_NAME + "Writer")
  public JpaItemWriter<User> migrationUserWriter() {
    return new JpaItemWriterBuilder<User>()
        .entityManagerFactory(entityManagerFactory)
        .usePersist(true)
        .build();
  }

  private DefaultLineMapper<User> defaultLineMapper() {
    DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
    defaultLineMapper.setLineTokenizer(new DelimitedLineTokenizer());
    defaultLineMapper.setFieldSetMapper(fieldSet ->
        User.create(
            fieldSet.readString(0),
            fieldSet.readString(1),
            fieldSet.readBoolean(2, "1")
        ));
    return defaultLineMapper;
  }
}
