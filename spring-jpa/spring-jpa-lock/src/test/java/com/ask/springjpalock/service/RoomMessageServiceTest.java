package com.ask.springjpalock.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomMessageServiceTest {

  @Autowired
  private RoomMessageService roomMessageService;

  /**
   * 1. roomMessage insert 시에 외래키로 인해 roomMember 의 S lock 획득 <br> 2. roomMember update 시에 X lock 획득하려하지만 이미 S lock 이 있어 데드락 발생
   */
  @DisplayName("데드락 발생")
  @Test
  void save() {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    while (true) {
      Future<?> submit1 = executorService.submit(
          () -> roomMessageService.save("book01", "rm01"));
      Future<?> submit2 = executorService.submit(
          () -> roomMessageService.save("book01", "rm01"));
      try {
        submit1.get();
        submit2.get();
      } catch (Exception e) {
        executorService.shutdown();
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * 1. roomMessage insert 시에 외래키로 인해 roomMember 의 S lock 획득 <br>
   * 2. 별도 트랜잭션에서 roomMember 를 업데이트할때 X lock 획득하므로 데드락 발생안함
   */
  @DisplayName("데드락 발생안함")
  @Test
  void saveWithoutDeadlock() {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    for (int i = 0; i < 10; i++) {
      Future<?> submit1 = executorService.submit(
          () -> roomMessageService.saveWithoutDeadlock("book01", "rm01"));
      Future<?> submit2 = executorService.submit(
          () -> roomMessageService.saveWithoutDeadlock("book01", "rm01"));
      try {
        submit1.get();
        submit2.get();
      } catch (Exception e) {
        executorService.shutdown();
        throw new RuntimeException(e);
      }
    }
  }

}
