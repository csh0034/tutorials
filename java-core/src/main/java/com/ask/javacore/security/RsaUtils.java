package com.ask.javacore.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RsaUtils {

	private static final int DEFAULT_KEY_SIZE = 2048;
	private static final String RSA_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA512withRSA";

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			generator.initialize(DEFAULT_KEY_SIZE, new SecureRandom());
			return generator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encrypt(String plainText, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] bytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String cipherText, PrivateKey privateKey) {
		try {
			byte[] bytes = Base64.getDecoder().decode(cipherText);
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String sign(String plainText, PrivateKey privateKey) {
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

	public static boolean verify(String plainText, String signature, PublicKey publicKey) {
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

  public static PublicKey convertPemToPublicKey(InputStream is) {
    try {
      String key = convertToString(is);

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

  public static PrivateKey convertPemToPrivateKey(InputStream is) {
    try {
      String key = convertToString(is);

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

  private static String convertToString(InputStream is) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    byte[] buffer = new byte[0xFFFF];
    for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
      os.write(buffer, 0, len);
    }
    return os.toString(StandardCharsets.UTF_8.name());
  }
}
