package com.ask.hibernatejasypt.config;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JasyptConfig {

  public static final String ENCRYPTOR_NAME = "encryptedString";
  public static final String ENCRYPTOR_REGISTERED_NAME = "hibernateStringEncryptor";

  private final JasyptProperties jasyptProperties;

  @Bean
  public HibernatePBEStringEncryptor hibernateStringEncryptor() {
    HibernatePBEStringEncryptor hibernateStringEncryptor = new HibernatePBEStringEncryptor();
    hibernateStringEncryptor.setRegisteredName(ENCRYPTOR_REGISTERED_NAME);
    hibernateStringEncryptor.setEncryptor(lenientPBStringEncryptor());
    return hibernateStringEncryptor;
  }

  private PBEStringEncryptor lenientPBStringEncryptor() {
    PooledPBEStringEncryptor stringEncryptor = new PooledPBEStringEncryptor();
    stringEncryptor.setAlgorithm("PBEWithMD5AndTripleDES");
    stringEncryptor.setPassword(jasyptProperties.getPassword());
    stringEncryptor.setPoolSize(4);
    return new LenientPBStringEncryptor(stringEncryptor);
  }

}
