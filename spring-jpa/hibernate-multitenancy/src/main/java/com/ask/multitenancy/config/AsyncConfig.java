package com.ask.multitenancy.config;

import com.ask.multitenancy.config.async.TenantAwareTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean
  public TaskDecorator TenantAwareTaskDecorator() {
    return new TenantAwareTaskDecorator();
  }

}
