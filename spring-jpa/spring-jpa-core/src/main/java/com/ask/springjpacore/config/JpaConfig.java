package com.ask.springjpacore.config;

import java.util.Properties;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class JpaConfig {

  @Bean
  public Advisor transactionAdviceAdvisor(TransactionManager transactionManager) {
    Properties properties = new Properties();
    properties.put("*", "PROPAGATION_REQUIRED");
    properties.put("get*", "PROPAGATION_REQUIRED,readOnly");
    properties.put("find*", "PROPAGATION_REQUIRED,readOnly");
    properties.put("is*", "PROPAGATION_REQUIRED,readOnly");
    properties.put("check*", "PROPAGATION_REQUIRED,readOnly");
    properties.put("exists*", "PROPAGATION_REQUIRED,readOnly");

    NameMatchTransactionAttributeSource txSource = new NameMatchTransactionAttributeSource();
    txSource.setProperties(properties);

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(* com..*.*Service.*(..))");

    return new DefaultPointcutAdvisor(pointcut, new TransactionInterceptor(transactionManager, txSource));
  }

}
