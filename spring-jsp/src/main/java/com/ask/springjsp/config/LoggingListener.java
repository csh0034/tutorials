package com.ask.springjsp.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingListener implements ServletRequestListener {

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    log.info("LoggingListener.requestInitialized");
  }

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
    log.info("LoggingListener.requestDestroyed");
  }

}
