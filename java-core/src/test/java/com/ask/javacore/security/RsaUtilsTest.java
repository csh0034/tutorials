package com.ask.javacore.security;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
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

class RsaUtilsTest extends BaseTest {

	private static final String PROPERTIES_PATH = "security/rsa/key.properties";
	private static final String PUBLIC_X509_PEM_PATH = "security/rsa/public-x509.pem";
	private static final String PRIVATE_PKCS8_PEM_PATH = "security/rsa/private-pkcs8.pem";
	private static final String PUBLIC_PKCS1_PEM_PATH = "security/rsa/public-pkcs1.pem";
	private static final String PRIVATE_PKCS1_PEM_PATH = "security/rsa/private-pkcs1.pem";

	private KeyPair keyPair;

	@BeforeEach
	void setup() {
		keyPair = RsaUtils.generateKeyPair();
	}

	@Order(1)
	@DisplayName("RSA KeyPair(PublicKey, PrivateKey) 생성")
	@Test
	void keyPair() {
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
		String encryptedText = RsaUtils.encrypt(publicKey, plainText);
		String decryptedText = RsaUtils.decrypt(privateKey, encryptedText);

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
		String signature = RsaUtils.sign(privateKey, plainText);
		boolean result = RsaUtils.verify(publicKey, plainText, signature);

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
    String signature = RsaUtils.sign(privateKey, plainText);
    boolean result = RsaUtils.verify(publicKey, invalidSigningKey, signature);

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
		String signature = RsaUtils.sign(privateKey, unixTimestampStr);

		// when 2
		// 수신측
		boolean result = RsaUtils.verify(publicKey, String.valueOf(unixTimestamp), signature);

		// then
		assertThat(result).isTrue();
	}

  @Order(8)
  @DisplayName("x509 public pem 파일을 InputStream 으로 읽어서 PublicKey 로 변환")
  @Test
  void convertPemToPublicKey() {
    // given
    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream is = classLoader.getResourceAsStream(PUBLIC_X509_PEM_PATH);

    // when
    PublicKey publicKey = RsaUtils.convertX509PemToPublicKey(is);

    // then
    System.out.println(publicKey);
    assertThat(publicKey).isNotNull();
  }

  @Order(9)
  @DisplayName("pkcs8 private pem 파일을 InputStream 으로 읽어서 PrivateKey 로 변환")
  @Test
  void convertPemToPrivateKey() {
    // given
    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream is = classLoader.getResourceAsStream(PRIVATE_PKCS8_PEM_PATH);

    // when
    PrivateKey privateKey = RsaUtils.convertPkcs8PemToPrivateKey(is);

    // then
    System.out.println(privateKey);
    assertThat(privateKey).isNotNull();
  }

  @Order(10)
  @DisplayName("pkcs1 public pem 파일을 InputStream 으로 읽어서 PublicKey 로 변환")
  @Test
  void convertPkcs1PemToPublicKey() {
    // given
    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream is = classLoader.getResourceAsStream(PUBLIC_PKCS1_PEM_PATH);

    // when
    PublicKey publicKey = RsaUtils.convertPkcs1PemToPublicKey(is);

    // then
    System.out.println(publicKey);
    assertThat(publicKey).isNotNull();
  }

  @Order(11)
  @DisplayName("pkcs1 private pem 파일을 InputStream 으로 읽어서 PrivateKey 로 변환")
  @Test
  void convertPkcs1PemToPrivateKey() {
    // given
    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream is = classLoader.getResourceAsStream(PRIVATE_PKCS1_PEM_PATH);

    // when
    PrivateKey privateKey = RsaUtils.convertPkcs1PemToPrivateKey(is);

    // then
    System.out.println(privateKey);
    assertThat(privateKey).isNotNull();
  }

  @Order(12)
  @DisplayName("pkcs8 private key 로 signing 하여 pkcs1 public key 로 검증")
  @Test
  void signingPkcs8AndVerifyPkcs1() {
    // given
    ClassLoader classLoader = this.getClass().getClassLoader();

    String unixTimestampStr = String.valueOf(System.currentTimeMillis());
    PrivateKey privateKey = RsaUtils.convertPkcs8PemToPrivateKey(classLoader.getResourceAsStream(PRIVATE_PKCS8_PEM_PATH));

    String signature = RsaUtils.sign(privateKey, unixTimestampStr);

    // when
    PublicKey publicKey = RsaUtils.convertPkcs1PemToPublicKey(classLoader.getResourceAsStream(PUBLIC_PKCS1_PEM_PATH));
    boolean result = RsaUtils.verify(publicKey, unixTimestampStr, signature);

    // then
    assertThat(result).isTrue();
  }

  @Order(13)
  @DisplayName("pkcs1 private key 로 signing 하여 pkcs1 public key 로 검증")
  @Test
  void signingPkcs1AndVerifyPkcs1() {
    // given
    ClassLoader classLoader = this.getClass().getClassLoader();

    String unixTimestampStr = String.valueOf(System.currentTimeMillis());
    PrivateKey privateKey = RsaUtils.convertPkcs1PemToPrivateKey(classLoader.getResourceAsStream(PRIVATE_PKCS1_PEM_PATH));

    String signature = RsaUtils.sign(privateKey, unixTimestampStr);

    // when
    PublicKey publicKey = RsaUtils.convertPkcs1PemToPublicKey(classLoader.getResourceAsStream(PUBLIC_PKCS1_PEM_PATH));
    boolean result = RsaUtils.verify(publicKey, unixTimestampStr, signature);

    // then
    assertThat(result).isTrue();
  }
}
