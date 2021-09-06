package com.ask.springjpalock;

import com.ask.springjpalock.entity.Company;
import com.ask.springjpalock.service.CompanyService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootTest
@Slf4j
public class PessimisticLockTest {

  @Autowired
  private CompanyService companyService;

  @DisplayName("낙관적 잠금일 경우 decrease 실패")
  @Test
  void test1() throws Exception {
    // given
    int numberOfExecute = 10;
    AtomicInteger successCount = new AtomicInteger();
    ExecutorService service = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(numberOfExecute);

    String companyId = companyService.saveCompany(Company.create("companyName"));

    // when
    for (int i = 0; i < numberOfExecute; i++) {
      int finalI = i;
      service.execute(() -> {
        try {
          companyService.decreaseCount(companyId);
          successCount.getAndIncrement();
          log.info("{} : 성공", finalI);
        } catch (ObjectOptimisticLockingFailureException oe) {
          // 복구처리 해줘야함
          log.error("{} : 충돌감지", finalI);
        }

        latch.countDown();
      });
    }
    latch.await();

    // then
    Company company = companyService.findById(companyId);
    log.info("limit : {}", company.getCount());

    log.info("successCount : {}", successCount.get());
  }

  @DisplayName("비관적 잠금일 경우 decrease 성공")
  @Test
  void test2() throws Exception {
    // given
    int numberOfExecute = 10;
    AtomicInteger successCount = new AtomicInteger();
    ExecutorService service = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(numberOfExecute);

    String companyId = companyService.saveCompany(Company.create("companyName"));

    // when
    for (int i = 0; i < numberOfExecute; i++) {
      int finalI = i;
      service.execute(() -> {
        companyService.decreaseCountWithPessimisticLock(companyId);
        successCount.getAndIncrement();
        log.info("{} : 성공", finalI);
        latch.countDown();
      });
    }
    latch.await();

    // then
    Company company = companyService.findById(companyId);
    log.info("limit : {}", company.getCount());

    log.info("successCount : {}", successCount.get());
  }
}