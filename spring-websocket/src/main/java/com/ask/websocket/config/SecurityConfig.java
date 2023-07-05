package com.ask.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages.nullDestMatcher().permitAll()
        .simpDestMatchers("/app/**").hasRole("USER")
        .simpSubscribeDestMatchers("/user/**", "/topic/**").hasRole("USER")
        .anyMessage().denyAll();
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

}
