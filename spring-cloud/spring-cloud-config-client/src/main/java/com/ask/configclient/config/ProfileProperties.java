package com.ask.configclient.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("profile")
@Getter
@Setter
@ToString
public class ProfileProperties {

  private String name;
  private String password;

}
