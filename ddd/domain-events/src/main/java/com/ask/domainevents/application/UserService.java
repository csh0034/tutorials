package com.ask.domainevents.application;

import com.ask.domainevents.domain.User1;
import com.ask.domainevents.domain.User1Repository;
import com.ask.domainevents.domain.User2;
import com.ask.domainevents.domain.User2Repository;
import com.ask.domainevents.domain.User3;
import com.ask.domainevents.domain.User3Repository;
import com.ask.domainevents.domain.User4;
import com.ask.domainevents.domain.User4Repository;
import com.ask.domainevents.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final User1Repository user1Repository;
  private final User2Repository user2Repository;
  private final User3Repository user3Repository;
  private final User4Repository user4Repository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void saveWithEvent(String name) {
    User1 user = user1Repository.save(new User1(name));
    eventPublisher.publishEvent(new DomainEvent(user.getId()));
  }

  @Transactional
  public void saveWithEvent2(String name) {
    User2 user = user2Repository.save(new User2(name));
    user.operation();
  }

  @Transactional
  public void saveWithEvent3(String name) {
    User3 user = new User3(name);
    user.operation();

    user3Repository.save(user);
  }

  @Transactional
  public void saveWithEvent4(String name) {
    User4 user = new User4(name);
    user.operation();

    user4Repository.save(user);
  }

}
