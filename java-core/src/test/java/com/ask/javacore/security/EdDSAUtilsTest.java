package com.ask.javacore.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.javacore.common.BaseTest;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EdDSAUtilsTest extends BaseTest {

  private KeyPair keyPair;

  @BeforeEach
  void setup() {
    keyPair = EdDSAUtils.generateKeyPair();
  }

  @Order(1)
  @DisplayName("EdDSA KeyPair(PublicKey, PrivateKey) 생성")
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
  @DisplayName("EdDSA 를 이용한 서명(sign) 및 검증(verify) 기능")
  @ParameterizedTest(name = "{index}. plainText : {0}")
  @ValueSource(strings = {"시그니처", "signature", "signature-test", "signature-검증"})
  void signAndVerify(String plainText) {
    // given
    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    // when
    String signature = EdDSAUtils.sign(privateKey, plainText);
    boolean result = EdDSAUtils.verify(publicKey, plainText, signature);

    // then
    print("signature : " + signature);

    assertThat(result).isTrue();
  }

  @Order(3)
  @DisplayName("String public key 를 PublicKey 객체로 변환")
  @Test
  void convertToPublicKey() {
    // given
    String stringPublicKey = "MCowBQYDK2VwAyEA+JyMBFZX5xrxBzM/S6z+5NiTBZt7eHZGjOm11sWDT30=";

    // when
    PublicKey publicKey = EdDSAUtils.convertToPublicKey(stringPublicKey);

    // then
    assertAll(
        () -> assertThat(publicKey).isNotNull(),
        () -> assertThat(stringPublicKey).isEqualTo(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
    );
  }

  @Order(4)
  @DisplayName("String private key 를 PublicKey 객체로 변환")
  @Test
  void convertToPrivateKey() {
    // given
    String stringPrivateKey = "MC4CAQAwBQYDK2VwBCIEIAzYgJLGR7q1Cp+yy4/zAfjWEJD5DWYIiKwOyzhXcM2C";

    // when
    PrivateKey privateKey = EdDSAUtils.convertToPrivateKey(stringPrivateKey);

    // then
    assertAll(
        () -> assertThat(privateKey).isNotNull(),
        () -> assertThat(stringPrivateKey).isEqualTo(Base64.getEncoder().encodeToString(privateKey.getEncoded()))
    );
  }

}
