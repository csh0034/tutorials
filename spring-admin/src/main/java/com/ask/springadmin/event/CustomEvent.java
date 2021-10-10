package com.ask.springadmin.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomEvent {

  @EventListener
  public void handleLoginSuccessEvent(AuthenticationSuccessEvent event) {
    log.info("AuthenticationSuccessEvent.getAuthentication : {}", event.getAuthentication());
  }

  @EventListener
  public void handleLogoutSuccessEvent(LogoutSuccessEvent event) {
    log.info("LogoutSuccessEvent.getAuthentication : {}", event.getAuthentication());
  }
}
