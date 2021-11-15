package com.ask.springcore.session;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class SessionAspect {

  private final HttpServletRequest request;

  @Around("execution(* com.ask.springcore.session.*.*(..))")
  public Object timeLog(ProceedingJoinPoint pjp) throws Throwable {
    log.info("session toString : {}", request.getSession(false));
    return pjp.proceed();
  }
}
