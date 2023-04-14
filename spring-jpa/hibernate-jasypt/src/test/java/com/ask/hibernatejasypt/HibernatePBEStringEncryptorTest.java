package com.ask.hibernatejasypt;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class HibernatePBEStringEncryptorTest {

  @Autowired
  private HibernatePBEStringEncryptor hibernatePBEStringEncryptor;

  @Test
  void decrypt() {
    log.info(hibernatePBEStringEncryptor.decrypt("HaTxPasvDps7+CDT56BfSA=="));
    log.info(hibernatePBEStringEncryptor.decrypt("kXxsPwl/wK5NzzFJThQzrGB5YidKqZu4"));
  }

}
