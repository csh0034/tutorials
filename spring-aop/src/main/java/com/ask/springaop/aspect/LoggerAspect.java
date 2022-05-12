package com.ask.springaop.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

  @Pointcut("execution(* com.ask..*Service.*(..))")
  public void all() {
  }

  @Before("all()")
  public void before() {
  }

  @Around("all()")
  public Object elapsed(ProceedingJoinPoint pjp) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    log.debug("1. args: {}", Arrays.toString(pjp.getArgs()));
    log.debug("2. name: {}", pjp.getSignature().getName());

    Object result = pjp.proceed();

    stopWatch.stop();

    log.debug("elapsed time: {} ms", stopWatch.getTotalTimeMillis());
    return result;
  }

  @After("all()")
  public void after() {
  }

}
