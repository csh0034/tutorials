package com.ask.springevent.listener;

import com.ask.springevent.entity.User;
import com.ask.springevent.event.UserSaveEvent;
import com.ask.springevent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSaveEventAnnotationDrivenListener {

  private final UserRepository userRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Async
  @EventListener
  public void handleUserSaveEvent(UserSaveEvent event) {
    log.info("invoke start point countDown()");
    event.getCountDownLatch().countDown();

    TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
    log.info("TransactionStatus : {}", transactionStatus);

    log.info("UserSaveEvent : {}", event);
    userRepository.save((User)event.getSource());

    log.info("invoke flush()");
    transactionStatus.flush();

    log.info("invoke end point countDown()");
    event.getCountDownLatch().countDown();
  }
}
