package com.ask.springintegrationamqp.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

  @Around("execution(* com.ask..handler.*Handler.*(..))")
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

}
