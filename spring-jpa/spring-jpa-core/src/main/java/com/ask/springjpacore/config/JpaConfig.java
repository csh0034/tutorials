package com.ask.springjpacore.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class JpaConfig {

  @Bean
  public Advisor transactionAdvisor(TransactionManager transactionManager) {
    NameMatchTransactionAttributeSource txSource = new NameMatchTransactionAttributeSource();

    txSource.addTransactionalMethod("*", new DefaultTransactionAttribute());
    addReadOnlyTransactionalMethods(txSource, "get*", "find*", "is*", "check*", "exists*");

    AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
    advisor.setAdvice(new TransactionInterceptor(transactionManager, txSource));
    advisor.setExpression("execution(* com..*.*Service.*(..))");
    return advisor;
  }

  private void addReadOnlyTransactionalMethods(NameMatchTransactionAttributeSource txSource, String... methodNames) {
    DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
    transactionAttribute.setReadOnly(true);

    for (String methodName : methodNames) {
      txSource.addTransactionalMethod(methodName, transactionAttribute);
    }
  }

}
