package com.ask.springcore.methodInvoking;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.MethodInvokingBean;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MethodInvoker;

@Configuration
@Slf4j
public class MethodInvokingConfig {

  public static final String SAMPLE_KEY1 = "sample.key1";
  public static final String SAMPLE_KEY2 = "sample.key2";
  public static final String SAMPLE_KEY3 = "sample.key3";

  @Bean
  public MethodInvoker sampleMethodInvoker() throws Exception {
    MethodInvoker methodInvoker = new MethodInvoker();
    methodInvoker.setTargetObject(System.getProperties());
    methodInvoker.setTargetMethod("putAll");

    Properties props = new Properties();
    props.setProperty(SAMPLE_KEY1, "sampleValue");

    methodInvoker.setArguments(props);

    methodInvoker.prepare();
    methodInvoker.invoke();

    return methodInvoker;
  }

  @Bean
  public MethodInvokingBean sampleMethodInvokingBean() {
    MethodInvokingBean bean = new MethodInvokingBean();
    bean.setTargetObject(System.getProperties());
    bean.setTargetMethod("putAll");

    Properties props = new Properties();
    props.setProperty(SAMPLE_KEY2, "sampleValue2");

    bean.setArguments(props);
    return bean;
  }

  @Bean
  public MethodInvokingFactoryBean sampleMethodInvokingFactoryBean() {
    MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
    bean.setTargetObject(System.getProperties());
    bean.setTargetMethod("putAll");

    Properties props = new Properties();
    props.setProperty(SAMPLE_KEY3, "sampleValue3");

    bean.setArguments(props);
    return bean;
  }

  @Bean
  public MethodInvokingBean userHomeMethodInvokingBean() {
    MethodInvokingBean bean = new MethodInvokingBean();
    bean.setTargetObject(System.getProperties());
    bean.setTargetMethod("getProperty");
    bean.setArguments("user.home");
    return bean;
  }

  @Bean
  public MethodInvokingFactoryBean javaVersionMethodInvokingFactoryBean() {
    MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
    bean.setTargetObject(System.getProperties());
    bean.setTargetMethod("getProperty");
    bean.setArguments("java.version");
    return bean;
  }
}
