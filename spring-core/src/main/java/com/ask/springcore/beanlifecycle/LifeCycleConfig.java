package com.ask.springcore.beanlifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1. postConstruct <br>
 * 2. afterPropertiesSet <br>
 * 3. initMethod <br>
 * 4. preDestroy <br>
 * 5. destroy <br>
 * 6. destroyMethod <br>
 */
@Configuration
public class LifeCycleConfig {

  @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
  public LifeCycle lifeCycle() {
    return new LifeCycle();
  }

  @Slf4j
  private static class LifeCycle implements InitializingBean, DisposableBean {

    @PostConstruct
    public void postConstruct() {
      log.info("postConstruct");
    }

    @Override
    public void afterPropertiesSet() {
      log.info("afterPropertiesSet");
    }

    public void initMethod() {
      log.info("initMethod");
    }

    @PreDestroy
    public void preDestroy() {
      log.info("preDestroy");
    }

    @Override
    public void destroy() {
      log.info("destroy");
    }

    public void destroyMethod() {
      log.info("destroyMethod");
    }

  }

}
