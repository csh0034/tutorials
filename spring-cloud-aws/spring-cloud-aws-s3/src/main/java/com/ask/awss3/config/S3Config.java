package com.ask.awss3.config;

import com.amazonaws.services.s3.AmazonS3;
import io.awspring.cloud.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

  private final AmazonS3 amazonS3;
  private final ApplicationContext applicationContext;

  @Bean
  public PathMatchingSimpleStorageResourcePatternResolver resourcePatternResolver() {
    return new PathMatchingSimpleStorageResourcePatternResolver(amazonS3, applicationContext);
  }

}
