package com.ask.springjpacore.config;

import java.util.Properties;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
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

    AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
    advisor.setAdvice(new TransactionInterceptor(transactionManager, txSource));
    advisor.setExpression("execution(* com..*.*Service.*(..))");
    return advisor;
  }

}
