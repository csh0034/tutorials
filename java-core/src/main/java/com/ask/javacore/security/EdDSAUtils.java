package com.ask.javacore.security;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public final class EdDSAUtils {

  private static final String ALGORITHM = "Ed25519";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private EdDSAUtils() {
  }

  public static KeyPair generateKeyPair() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
      return generator.generateKeyPair();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String sign(PrivateKey privateKey, String plainText) {
    try {
      Signature sig = Signature.getInstance(ALGORITHM);
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
      Signature sig = Signature.getInstance(ALGORITHM);
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
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
      return keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PrivateKey convertToPrivateKey(String stringPrivateKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
      return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytePrivateKey));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
