package com.ask.springjasypt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("custom")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
@ToString
public class CustomProperties {

  private final String name;
  private final String password;

}
