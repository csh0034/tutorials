package com.ask.hibernatejasypt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PBEStringCleanablePasswordEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;

@Slf4j
@RequiredArgsConstructor
public class LenientPBStringEncryptor implements PBEStringCleanablePasswordEncryptor {

  private final PooledPBEStringEncryptor pooledPBEStringEncryptor;

  @Override
  public String encrypt(String message) {
    return pooledPBEStringEncryptor.encrypt(message);
  }

  @Override
  public String decrypt(String encryptedMessage) {
    try {
      return pooledPBEStringEncryptor.decrypt(encryptedMessage);
    } catch (Exception ex) {
      log.warn("Decryption Failed, encryptedMessage: {}", encryptedMessage);
      return encryptedMessage;
    }
  }

  @Override
  public void setPasswordCharArray(char[] password) {
    pooledPBEStringEncryptor.setPasswordCharArray(password);
  }

  @Override
  public void setPassword(String password) {
    pooledPBEStringEncryptor.setPassword(password);
  }

}
