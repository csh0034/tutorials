package com.ask.springjasypt.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PBEStringCleanablePasswordEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;

@Slf4j
public class LenientPBStringEncryptor implements PBEStringCleanablePasswordEncryptor {

  private final PooledPBEStringEncryptor pooledPBEStringEncryptor;

  public LenientPBStringEncryptor(StringEncryptor stringEncryptor) {
    this.pooledPBEStringEncryptor = (PooledPBEStringEncryptor) stringEncryptor;
  }

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
