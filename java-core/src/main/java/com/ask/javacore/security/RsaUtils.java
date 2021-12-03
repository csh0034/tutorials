package com.ask.javacore.security;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public final class RsaUtils {

  private static final int DEFAULT_KEY_SIZE = 2048;
  private static final String RSA_ALGORITHM = "RSA";
  private static final String SIGNATURE_ALGORITHM = "SHA512withRSA";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static KeyPair generateKeyPair() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
      generator.initialize(DEFAULT_KEY_SIZE, new SecureRandom());
      return generator.generateKeyPair();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String encrypt(PublicKey publicKey, String plainText) {
    try {
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] bytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(bytes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String decrypt(PrivateKey privateKey, String cipherText) {
    try {
      byte[] bytes = Base64.getDecoder().decode(cipherText);
      Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String sign(PrivateKey privateKey, String plainText) {
    try {
      Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
      sig.initSign(privateKey);
      sig.update(plainText.getBytes(StandardCharsets.UTF_8));
      byte[] signature = sig.sign();
      return Base64.getEncoder().encodeToString(signature);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean verify(PublicKey publicKey, String plainText, String signature) {
    try {
      Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
      sig.initVerify(publicKey);
      sig.update(plainText.getBytes());
      if (!sig.verify(Base64.getDecoder().decode(signature))) {
        return false;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  public static PublicKey convertToPublicKey(String stringPublicKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
      return keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PrivateKey convertToPrivateKey(String stringPrivateKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
      return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytePrivateKey));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PublicKey convertX509PemToPublicKey(InputStream is) {
    try {
      String key = IOUtils.toString(is, StandardCharsets.UTF_8);

      String pem = key
          .replace("-----BEGIN PUBLIC KEY-----", "")
          .replaceAll(System.lineSeparator(), "")
          .replace("-----END PUBLIC KEY-----", "");

      byte[] bytePublicKey = Base64.getDecoder().decode(pem.getBytes());
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      return keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PrivateKey convertPkcs8PemToPrivateKey(InputStream is) {
    try {
      String key = IOUtils.toString(is, StandardCharsets.UTF_8);

      String pem = key
          .replace("-----BEGIN PRIVATE KEY-----", "")
          .replaceAll(System.lineSeparator(), "")
          .replace("-----END PRIVATE KEY-----", "");

      byte[] bytePrivateKey = Base64.getDecoder().decode(pem.getBytes());
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytePrivateKey));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PublicKey convertPkcs1PemToPublicKey(InputStream is) {
    try {
      PEMParser pemParser = new PEMParser(new InputStreamReader(Objects.requireNonNull(is)));
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
      SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
      return converter.getPublicKey(publicKeyInfo);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PrivateKey convertPkcs1PemToPrivateKey(InputStream is) {
    try {
      PEMParser pemParser = new PEMParser(new InputStreamReader(Objects.requireNonNull(is)));
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
      PEMKeyPair keyPair = (PEMKeyPair) pemParser.readObject();
      return converter.getPrivateKey(keyPair.getPrivateKeyInfo());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
