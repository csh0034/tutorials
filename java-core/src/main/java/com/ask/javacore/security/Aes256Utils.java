package com.ask.javacore.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Aes256Utils {

  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

  private static final String SECRET_KEY = "0123456789-0123456789-0123456789";
  private static final String IV_KEY = SECRET_KEY.substring(0, 16);

  private static final SecretKeySpec KEY_SPEC;
  private static final IvParameterSpec IV_SPEC;

  static {
    KEY_SPEC = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    IV_SPEC = new IvParameterSpec(IV_KEY.getBytes(StandardCharsets.UTF_8));
  }

  public static String encrypt(String plainText) {
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, KEY_SPEC, IV_SPEC);
      byte[] bytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(bytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String decrypt(String cipherText) {
    try {
      byte[] bytes = Base64.getDecoder().decode(cipherText);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, KEY_SPEC, IV_SPEC);
      return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
