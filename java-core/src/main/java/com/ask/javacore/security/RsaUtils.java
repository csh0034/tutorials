package com.ask.javacore.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RsaUtils {

	public static final String CHARSET = "UTF-8";

	private static final int DEFAULT_KEY_SIZE = 2048;
	private static final String RSA_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA512withRSA";

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			generator.initialize(DEFAULT_KEY_SIZE, new SecureRandom());
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String encrypt(String plainText, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] bytes = cipher.doFinal(plainText.getBytes(CHARSET));
			return Base64.getEncoder().encodeToString(bytes);
		} catch (NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException
			| BadPaddingException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String cipherText, PrivateKey privateKey) {
		try {
			byte[] bytes = Base64.getDecoder().decode(cipherText);
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(bytes), CHARSET);
		} catch (NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException
			| BadPaddingException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String sign(String plainText, PrivateKey privateKey) {
		try {
			Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initSign(privateKey);
			sig.update(plainText.getBytes(CHARSET));
			byte[] signature = sig.sign();
			return Base64.getEncoder().encodeToString(signature);
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) {
		try {
			Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(plainText.getBytes());
			if (!sig.verify(Base64.getDecoder().decode(signature))) {
				throw new RuntimeException("Invalid Signature : " + signature);
			}
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public static PublicKey convertToPublicKey(String stringPublicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
			return keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static PrivateKey convertToPrivateKey(String stringPrivateKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			byte[] bytePublicKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
			return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytePublicKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
