package com.ask.springasync;

import com.ask.springasync.service.SyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAsyncApplicationTest {

  @Autowired
  private SyncService syncService;

  @Test
  void printTimestamp() {
    syncService.printTimestamp();
  }

}
