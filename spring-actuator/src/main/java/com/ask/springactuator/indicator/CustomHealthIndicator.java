package com.ask.springactuator.indicator;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

/**
 * HealthIndicator 로 끝날 경우 앞에 이름이 노출됨
 */
@Component
public class CustomHealthIndicator extends AbstractHealthIndicator {

  @Override
  protected void doHealthCheck(Builder builder) {
    builder.up()
        .withDetail("app", "running")
        .withDetail("error", "nothing");
  }
}
