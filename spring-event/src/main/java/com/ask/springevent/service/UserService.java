package com.ask.springevent.service;

import com.ask.springevent.entity.User;
import com.ask.springevent.event.TransactionEvent;
import com.ask.springevent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final ApplicationEventPublisher publisher;
  private final UserRepository userRepository;

  public void rollback() {
    publisher.publishEvent(new TransactionEvent("source!!!", "rollback"));

    throw new RuntimeException("rollback!!");
  }

  public void saveTestUser() {
    publisher.publishEvent(new TransactionEvent("source!!!", "saveTestUser"));

    User user = userRepository.save(User.create(String.valueOf(System.currentTimeMillis())));
  }
}
