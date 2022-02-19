package com.ask.openfeign.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("feign.custom")
@Setter
@Getter
public class FeignCustomProperties {

  private String googleUrl;

}
