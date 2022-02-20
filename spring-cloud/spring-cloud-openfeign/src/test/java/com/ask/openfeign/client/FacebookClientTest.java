package com.ask.openfeign.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class FacebookClientTest {

  @Autowired
  private FacebookClient facebookClient;

  @Test
  void notFound() {
    String notFound = facebookClient.notFound();
    log.info("notFound : {}", notFound);
  }

}
