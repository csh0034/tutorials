package com.ask.springjpacore;

import com.ask.springjpacore.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@AutoConfigureTestEntityManager
@Slf4j
class TransactionTemplateTest {

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  void executeWithoutResult() {
    log.info("before transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());

    transactionTemplate.executeWithoutResult((status) -> {
      log.info("in transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());
      testEntityManager.persist(Company.create("transaction"));

    });

    log.info("after transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Test
  void executeWithoutResultRollback() {
    log.info("before transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());

    transactionTemplate.executeWithoutResult((status) -> {
      log.info("in transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());
      testEntityManager.persist(Company.create("transaction"));
      status.setRollbackOnly();
    });

    log.info("after transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());
  }

}
