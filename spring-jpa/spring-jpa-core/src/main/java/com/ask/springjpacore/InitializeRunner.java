package com.ask.springjpacore;

import com.ask.springjpacore.entity.TsidUser;
import com.ask.springjpacore.entity.auto.Auto;
import com.ask.springjpacore.entity.table.Notice;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitializeRunner implements ApplicationRunner {

  private final EntityManager entityManager;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    entityManager.persist(new Auto());
    entityManager.persist(new Auto());
    entityManager.persist(new Notice());
    entityManager.persist(new Notice());

    entityManager.persist(new TsidUser());
    entityManager.persist(new TsidUser());
  }

}
