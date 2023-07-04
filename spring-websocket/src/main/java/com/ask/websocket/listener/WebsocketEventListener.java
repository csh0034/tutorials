package com.ask.websocket.listener;

import java.security.Principal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
public class WebsocketEventListener {

  @EventListener
  public void handleSessionConnect(SessionConnectEvent event) {
    log.info("SessionConnectEvent, username: {}", extractUsername(event));
  }

  @EventListener
  public void handleSessionConnected(SessionConnectedEvent event) {
    log.info("SessionConnectedEvent, username: {}", extractUsername(event));
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    log.info("SessionDisconnectEvent, username: {}", extractUsername(event));
  }

  @EventListener
  public void handleSubscribe(SessionSubscribeEvent event) {
    log.info("SessionSubscribeEvent, username: {}", extractUsername(event));
  }

  @EventListener
  public void handleUnsubscribe(SessionUnsubscribeEvent event) {
    log.info("SessionUnsubscribeEvent, username: {}", extractUsername(event));
  }

  private String extractUsername(AbstractSubProtocolEvent event) {
   return Optional.ofNullable(event.getUser())
       .map(Principal::getName)
       .orElse("");
  }

}
