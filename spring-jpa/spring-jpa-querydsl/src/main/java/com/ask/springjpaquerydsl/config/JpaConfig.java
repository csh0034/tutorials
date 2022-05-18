package com.ask.springjpaquerydsl.config;

import com.querydsl.codegen.ClassPathUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig implements InitializingBean {

  private static final String DOMAIN_PACKAGE = "com.ask.springjpaquerydsl.entity";

  @Bean
  public JPAQueryFactory queryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    ClassPathUtils.scanPackage(Thread.currentThread().getContextClassLoader(), DOMAIN_PACKAGE);
  }

}
