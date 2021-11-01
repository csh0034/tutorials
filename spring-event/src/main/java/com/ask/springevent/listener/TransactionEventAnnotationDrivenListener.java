package com.ask.springevent.listener;

import com.ask.springevent.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class TransactionEventAnnotationDrivenListener {

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void beforeCommit(TransactionEvent event) {
    log.info("TransactionEvent (BEFORE_COMMIT) : {}", event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void afterCommit(TransactionEvent event) {
    log.info("TransactionEvent (AFTER_COMMIT) : {}", event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void afterRollback(TransactionEvent event) {
    log.info("TransactionEvent (AFTER_ROLLBACK) : {}", event);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void afterCompletion(TransactionEvent event) {
    log.info("TransactionEvent (AFTER_COMPLETION) : {}", event);
  }
}
