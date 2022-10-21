package com.ask.openfeign.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;

/**
 * property 로 설정 가능함.
 */
//@Configuration
public class Resilience4JConfig {

  private static final int TIME_LIMITER_TIMEOUT_SECOND = 5;

  @Bean
  public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(TIME_LIMITER_TIMEOUT_SECOND)).build())
        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
        .build());
  }

}
