package com.ask.javacore.security;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ask.javacore.common.BaseTest;
import com.ask.javacore.util.PropertiesUtils;

class RsaTest extends BaseTest {

	private static final String PROPERTIES_PATH = "security/rsa/key.properties";

	private KeyPair keyPair;

	@BeforeEach
	void setup() {
		keyPair = RsaUtils.generateKeyPair();
	}

	@Order(1)
	@DisplayName("RSA KeyPair(PublicKey, PrivateKey) 생성")
	@Test
	void KeyPair() {
		// when
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// then
		print(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
		print(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

		assertThat(publicKey).isNotNull();
		assertThat(privateKey).isNotNull();
	}

	@Order(2)
	@DisplayName("RSA 를 이용한 encrypt, decrypt")
	@ParameterizedTest(name = "{index}. plainText : {0}")
	@ValueSource(strings = {"a", "bcasdf", "123154", "1356cccgfasd"})
	void encryptAndDecrypt(String plainText) {
		// given
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// when
		String encryptedText = RsaUtils.encrypt(plainText, publicKey);
		String decryptedText = RsaUtils.decrypt(encryptedText, privateKey);

		// then
		print("encryptedText : " + encryptedText);
		print("decryptedText : " + decryptedText);

		assertThat(plainText).isEqualTo(decryptedText);
	}

	@Order(3)
	@DisplayName("RSA 를 이용한 서명(sign) 및 검증(verify) 기능")
	@ParameterizedTest(name = "{index}. plainText : {0}")
	@ValueSource(strings = {"시그니처", "signature", "signature-test", "signature-검증"})
	void signAndVerify(String plainText) {
		// given
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// when
		String signature = RsaUtils.sign(plainText, privateKey);
		boolean result = RsaUtils.verify(plainText, signature, publicKey);

		// then
		print("signature : " + signature);

		assertThat(result).isTrue();
	}

  @Order(4)
  @DisplayName("RSA 를 이용한 서명(sign) 및 검증(verify) 실패 검증")
  @ParameterizedTest(name = "{index}. plainText : {0}")
  @ValueSource(strings = {"시그니처", "signature", "signature-test", "signature-검증"})
  void signAndVerifyFailed(String plainText) {
    // given
    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    String invalidSigningKey = "invalidSigningKey";

    // when
    String signature = RsaUtils.sign(plainText, privateKey);
    boolean result = RsaUtils.verify(invalidSigningKey, signature, publicKey);

    // then
    print("signature : " + signature);

    assertThat(result).isFalse();
  }

	@Order(5)
	@DisplayName("String public key를 PublicKey 객체로 변환")
	@Test
	void convertToPublicKey() {
		// given
		Properties properties = PropertiesUtils.load(PROPERTIES_PATH);
		String stringPublicKey = properties.getProperty("public");

		// when
		PublicKey publicKey = RsaUtils.convertToPublicKey(stringPublicKey);

		// then
		assertAll(
			() -> assertThat(publicKey).isNotNull(),
			() -> assertThat(stringPublicKey).isEqualTo(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
		);
	}

	@Order(6)
	@DisplayName("String private key를 PublicKey 객체로 변환")
	@Test
	void convertToPrivateKey() {
		// given
		Properties properties = PropertiesUtils.load(PROPERTIES_PATH);
		String stringPrivateKey = properties.getProperty("private");

		// when
		PrivateKey privateKey = RsaUtils.convertToPrivateKey(stringPrivateKey);

		// then
		assertAll(
			() -> assertThat(privateKey).isNotNull(),
			() -> assertThat(stringPrivateKey).isEqualTo(Base64.getEncoder().encodeToString(privateKey.getEncoded()))
		);
	}

	@Order(7)
	@DisplayName("RSA 를 이용한 unixTimestamp 서명(sign) 및 검증(verify) 기능")
	@Test
	void signAndVerifyWithTimestamp() {
		// given
		long unixTimestamp = System.currentTimeMillis();
		String unixTimestampStr = String.valueOf(unixTimestamp);

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// when
		// 송신측
		String signature = RsaUtils.sign(unixTimestampStr, privateKey);

		// when 2
		// 수신측
		boolean result = RsaUtils.verify(String.valueOf(unixTimestamp), signature, publicKey);

		// then
		assertThat(result).isTrue();
	}
}
