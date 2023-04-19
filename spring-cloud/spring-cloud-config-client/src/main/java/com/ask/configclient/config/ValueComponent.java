package com.ask.configclient.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
@Getter
@ToString
public class ValueComponent {

  @Value("${profile.name}")
  private String profileName;

}
