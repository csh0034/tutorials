package com.ask.domainevents.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class DomainEventListener {

  @TransactionalEventListener
  public void handle(DomainEvent domainEvent) {
    log.info("handle.. event: {}", domainEvent);
  }

}
