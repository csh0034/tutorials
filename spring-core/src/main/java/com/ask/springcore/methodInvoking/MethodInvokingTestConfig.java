package com.ask.springcore.methodInvoking;

import static com.ask.springcore.methodInvoking.MethodInvokingConfig.SAMPLE_KEY1;
import static com.ask.springcore.methodInvoking.MethodInvokingConfig.SAMPLE_KEY2;
import static com.ask.springcore.methodInvoking.MethodInvokingConfig.SAMPLE_KEY3;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MethodInvokingTestConfig {

  private final ApplicationContext appCtx;

  @PostConstruct
  public void init() {
    log.info("sampleMethodInvoker : {}", appCtx.getBean("sampleMethodInvoker"));
    log.info("sampleMethodInvokingBean : {}", appCtx.getBean("sampleMethodInvokingBean"));
    log.info("sampleMethodInvokingFactoryBean : {}", appCtx.getBean("&sampleMethodInvokingFactoryBean"));

    log.info("userHomeMethodInvokingBean : {}", appCtx.getBean("userHomeMethodInvokingBean"));
    log.info("javaVersionMethodInvokingFactoryBean : {}", appCtx.getBean("javaVersionMethodInvokingFactoryBean"));

    log.info("SAMPLE_KEY1 : {}", System.getProperty(SAMPLE_KEY1));
    log.info("SAMPLE_KEY2 : {}", System.getProperty(SAMPLE_KEY2));
    log.info("SAMPLE_KEY3 : {}", System.getProperty(SAMPLE_KEY3));
  }
}
