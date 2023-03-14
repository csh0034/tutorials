package com.ask.springcore.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class SqlConfig {

  @Bean
  public PropertiesFactoryBean sqlProperties() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(new ClassPathResource("sql/sql.xml"));
    return bean;
  }

}
