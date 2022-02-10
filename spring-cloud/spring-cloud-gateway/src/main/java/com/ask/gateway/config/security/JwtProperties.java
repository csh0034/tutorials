package com.ask.gateway.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Setter
@Getter
public class JwtProperties {

  private String secret;
  private long expirationSecond;

}
