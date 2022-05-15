package com.ask.springjasypt;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class JasyptTest {

  @Resource(name = "jasyptStringEncryptor")
  private StringEncryptor stringEncryptor;

  @Test
  void encrypt() {
    log.info("ASk: {} ", stringEncryptor.encrypt("ASk"));
    log.info("1234: {} ", stringEncryptor.encrypt("1234"));
  }

  @Test
  void decrypt() {
    String encryptedName = "g1gdSIYixbiXV79I/Uz36qOsIDfAHLsGy0kI1zyqM4EHBlc2SK4/ym4z3eixp5YC";
    String encryptedPassword = "WB65brCwKnwgELtHzXL9docoEazIEUMAg5Ih6ezfyTEhsHu2P04vRjerqsu+RILo";

    log.info("{}: {}", encryptedName, stringEncryptor.decrypt(encryptedName));
    log.info("{}: {}", encryptedPassword, stringEncryptor.decrypt(encryptedPassword));
  }

}
