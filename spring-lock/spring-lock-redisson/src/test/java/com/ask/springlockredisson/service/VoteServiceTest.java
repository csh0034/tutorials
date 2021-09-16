package com.ask.springlockredisson.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springlockredisson.entity.Vote;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class VoteServiceTest {

  private static final int THREAD_SIZE = 10;

  CountDownLatch countDownLatch = new CountDownLatch(THREAD_SIZE);
  ExecutorService service = Executors.newFixedThreadPool(THREAD_SIZE);

  @Autowired
  VoteService voteService;

  @AfterEach
  void tearDown() {
    voteService.deleteAll();
  }

  @Test
  void voteWithoutLock() throws Exception {
    // before
    List<Vote> votes = voteService.findAll();
    votes.forEach(vote -> log.info("vote : {}", vote));

    // given
    String voter = "voter1";
    String candidate = "candidate1";

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        voteService.voteWithoutLock(voter, candidate);
        countDownLatch.countDown();
      });
    }

    countDownLatch.await();

    // then
    List<Vote> votes2 = voteService.findAll();
    votes2.forEach(vote -> log.info("vote : {}", vote));

    assertThat(votes2.size()).isGreaterThan(1);
  }

  @Test
  void voteWithLock() throws Exception {
    // given
    String voter = "voter2";
    String candidate = "candidate2";

    // when
    for (int i = 0; i < THREAD_SIZE; i++) {
      service.execute(() -> {
        voteService.voteWithLock(voter, candidate);
        countDownLatch.countDown();
      });
    }

    countDownLatch.await();

    // then
    List<Vote> votes = voteService.findAll();
    votes.forEach(vote -> log.info("vote : {}", vote));

    assertThat(votes.size()).isEqualTo(1);
  }
}