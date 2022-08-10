
package com.ask.springaop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggerAnnotationAspect {

  @Around("@annotation(logger)")
  public Object proceed(ProceedingJoinPoint pjp, Logger logger) throws Throwable {
    String value = logger.value();

    log.info("Logger: {}", value);

    return pjp.proceed();
  }

}
