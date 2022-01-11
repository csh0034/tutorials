package com.ask.javacore.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.javacore.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Aes256UtilsTest extends BaseTest {

  @Order(1)
  @DisplayName("AES 를 이용한 encrypt, decrypt")
  @ParameterizedTest(name = "{index}. plainText : {0}")
  @ValueSource(strings = {"a", "bcasdf", "123154", "1356cccgfasd"})
  void encryptAndDecrypt(String plainText) {
    // when
    String encryptedText = Aes256Utils.encrypt(plainText);
    String decryptedText = Aes256Utils.decrypt(encryptedText);

    // then
    print("encryptedText : " + encryptedText);
    print("decryptedText : " + decryptedText);

    assertThat(plainText).isEqualTo(decryptedText);
  }
}
