package com.ask.springwebflux.config.cache;

import static java.util.stream.Collectors.joining;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ReactorCacheAspect {

  private final ReactorCacheManager reactorCacheManager;

  @Pointcut("@annotation(ReactorCache)")
  public void pointcut() {
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Around("pointcut()")
  public Object around(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
    Type rawType = parameterizedType.getRawType();

    if (!(rawType.equals(Mono.class) || rawType.equals(Flux.class))) {
      throw new IllegalArgumentException(String.format("method[%s] not return type Mono/Flux", method.getName()));
    }

    ReactorCache reactorCache = method.getAnnotation(ReactorCache.class);
    String cacheName = reactorCache.value();
    Object[] args = joinPoint.getArgs();

    ThrowingSupplier retriever = () -> joinPoint.proceed(args);

    if (rawType.equals(Mono.class)) {
      Type returnTypeInsideMono = parameterizedType.getActualTypeArguments()[0];
      Class<?> returnClass = ResolvableType.forType(returnTypeInsideMono).resolve();

      return reactorCacheManager.findCachedMono(cacheName, generateKey(args), retriever, returnClass)
          .doOnError(e -> log.error("Failed to processing mono cache. method: " + method.getName(), e))
          .subscribeOn(Schedulers.boundedElastic());
    } else {
      return reactorCacheManager.findCachedFlux(cacheName, generateKey(args), retriever)
          .doOnError(e -> log.error("Failed to processing flux cache. method: " + method.getName(), e))
          .subscribeOn(Schedulers.boundedElastic());
    }
  }

  private String generateKey(Object... objects) {
    return Arrays.stream(objects)
        .map(obj -> obj == null ? "" : obj.toString())
        .collect(joining(":"));
  }

}
