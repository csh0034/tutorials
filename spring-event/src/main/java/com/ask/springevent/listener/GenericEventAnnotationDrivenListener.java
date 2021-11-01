package com.ask.springevent.listener;

import com.ask.springevent.event.GenericEvent;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenericEventAnnotationDrivenListener {

  private final EntityManager em;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @EventListener(condition = "#event.needToPersist")
  public void handleGenericEvent(GenericEvent<?> event) {
    log.info("GenericEvent<User> : {}", event);
    em.persist(event.getEntity());
  }
}
