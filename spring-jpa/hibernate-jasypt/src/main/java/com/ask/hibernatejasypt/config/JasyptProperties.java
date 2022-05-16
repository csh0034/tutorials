package com.ask.hibernatejasypt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("jasypt")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
@ToString
public class JasyptProperties {

  private final String password;

}
