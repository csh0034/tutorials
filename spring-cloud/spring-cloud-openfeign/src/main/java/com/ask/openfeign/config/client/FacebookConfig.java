package com.ask.openfeign.config.client;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * 주의! : @Configuration 추가하면 default 설정에 영향을 미치므로 추가하면 안됨 <br> 만약 추가해야할 경우 @ComponentScan 대상에서 제외시켜야함
 */
public class FacebookConfig {

  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor("ASk", "1234567890");
  }

  @Bean
  public Logger.Level facebookLoggerLevel() {
    return Logger.Level.HEADERS;
  }

}
