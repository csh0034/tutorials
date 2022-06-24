package com.ask.springcore;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootTest
public class DynamicBeanRegistrationTest {

  @DisplayName("GenericApplicationContext 사용시에")
  @Nested
  class GenericWebApplicationContextTest {

    @DisplayName("registerBean 으로 등록하면 afterPropertiesSet 호출함")
    @Test
    void registerBean(@Autowired GenericApplicationContext context) {
      // given
      String beanName = RandomStringUtils.randomAlphanumeric(10);

      // when
      context.registerBean(beanName, TestLifeCycle.class);

      TestLifeCycle testLifeCycle = context.getBean(beanName, TestLifeCycle.class);
      testLifeCycle.print();
    }

    @DisplayName("registerBeanDefinition 으로 등록하면 afterPropertiesSet 호출함")
    @Test
    void registerBeanDefinition(@Autowired GenericApplicationContext context) {
      // given
      String beanName = RandomStringUtils.randomAlphanumeric(10);

      BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TestLifeCycle.class)
          .getBeanDefinition();

      // when
      context.registerBeanDefinition(beanName, beanDefinition);

      TestLifeCycle testLifeCycle = context.getBean(beanName, TestLifeCycle.class);
      testLifeCycle.print();
    }

  }

  @DisplayName("ConfigurableBeanFactoryTest 사용시에")
  @Nested
  class ConfigurableBeanFactoryTest {

    @DisplayName("registerSingleton 으로 등록하면 afterPropertiesSet 호출안함")
    @Test
    void registerSingleton(@Autowired ConfigurableBeanFactory beanFactory) {
      // given
      String beanName = RandomStringUtils.randomAlphanumeric(10);

      // when
      beanFactory.registerSingleton(beanName, new TestLifeCycle());

      TestLifeCycle testLifeCycle = beanFactory.getBean(beanName, TestLifeCycle.class);
      testLifeCycle.print();
    }

  }

  @Slf4j
  private static class TestLifeCycle implements InitializingBean {

    public void print() {
      log.info("print...");
    }

    @PostConstruct
    public void postConstruct() {
      log.info("postConstruct...");
    }

    @Override
    public void afterPropertiesSet() {
      log.info("afterPropertiesSet...");
    }

  }

}
