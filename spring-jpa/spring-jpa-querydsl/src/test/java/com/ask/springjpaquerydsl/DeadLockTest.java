package com.ask.springjpaquerydsl;

import static com.ask.springjpaquerydsl.entity.QParent.parent;
import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpaquerydsl.entity.Parent;
import com.ask.springjpaquerydsl.entity.Parent.Status;
import com.ask.springjpaquerydsl.entity.QParent;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@Slf4j
class DeadLockTest {

  @Autowired
  private JPAQueryFactory queryFactory;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Test
  void test() throws Exception {
    int numberOfExecute = 50;
    ExecutorService service = Executors.newFixedThreadPool(numberOfExecute);
    CountDownLatch latch = new CountDownLatch(numberOfExecute);

    for (int i = 0; i < numberOfExecute; i++) {
      service.execute(() -> {
        transactionTemplate.executeWithoutResult(status -> queryFactory
            .update(parent)
            .set(parent.totalCount, parent.totalCount.add(1))
            .set(parent.status,
                new CaseBuilder()
                    .when(parent.totalCount.add(1).eq(150L))
                    .then(Status.END)
                    .otherwise(parent.status))
            .where(parent.id.eq("p01"))
            .execute());

        latch.countDown();
      });
    }

    latch.await();

    Parent parent = queryFactory.selectFrom(QParent.parent)
        .where(QParent.parent.id.eq("p01"))
        .fetchFirst();

    log.info("parent : {}", parent);
    assertThat(parent.getTotalCount()).isEqualTo(150);
  }
}
