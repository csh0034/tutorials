package com.ask.hibernatejasypt.config.crypto;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;

@Converter
@Slf4j
@RequiredArgsConstructor
public class StringEncryptConverter implements AttributeConverter<String, String> {

  private final HibernatePBEStringEncryptor hibernateStringEncryptor;

  @Override
  public String convertToDatabaseColumn(String s) {
    return hibernateStringEncryptor.getEncryptor().encrypt(s);
  }

  @Override
  public String convertToEntityAttribute(String s) {
    if (s == null) {
      return null;
    }
    return hibernateStringEncryptor.getEncryptor().decrypt(s);
  }

}
