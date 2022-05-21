package com.ask.springjasypt;

import static com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties.bindConfigProps;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ulisesbocchio.jasyptspringboot.configuration.StringEncryptorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

@Slf4j
public class StringEncryptorTest {

  private static StringEncryptor stringEncryptor;

  @BeforeAll
  static void beforeAll() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("jasypt.encryptor.password", "password...");

    stringEncryptor = new StringEncryptorBuilder(bindConfigProps(env), "jasypt.encryptor")
        .build();
  }

  @Test
  void encryptAndDecrypt() {
    String name = "ASk";

    String encryptedName = stringEncryptor.encrypt(name);
    String decryptedName = stringEncryptor.decrypt(encryptedName);

    log.info("encryptedName: {}", encryptedName);
    log.info("decryptedName: {}", decryptedName);

    assertThat(decryptedName).isEqualTo(name);
  }

}
