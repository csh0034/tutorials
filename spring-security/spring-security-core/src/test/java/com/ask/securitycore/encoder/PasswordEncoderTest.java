package com.ask.securitycore.encoder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

@Slf4j
class PasswordEncoderTest {

  private static final String RAW_PASSWORD = "1111";

  /**
   * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
   */
  @Nested
  class DelegatingPasswordEncoderTest {

    @Test
    void encodeAndMatches() {
      String encodingId = "argon2";

      Map<String, PasswordEncoder> encoders = new HashMap<>();
      encoders.put(encodingId, new Argon2PasswordEncoder());

      PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);

      String encodedPassword = passwordEncoder.encode(RAW_PASSWORD);
      log.info("result: {}", encodedPassword);

      boolean matches = passwordEncoder.matches(RAW_PASSWORD, encodedPassword);
      assertThat(matches).isTrue();
    }

  }

  @Nested
  class Pbkdf2PasswordEncoderTest {

    @Test
    void encodeAndMatches() {
      Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder("secretKey");
      passwordEncoder.setAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);

      String encodedPassword = passwordEncoder.encode(RAW_PASSWORD);
      log.info("result: {}", encodedPassword);

      boolean matches = passwordEncoder.matches(RAW_PASSWORD, encodedPassword);
      assertThat(matches).isTrue();
    }

  }

  @Nested
  class Argon2PasswordEncoderTest {

    @Test
    void encodeAndMatches() {
      PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

      String encodedPassword = passwordEncoder.encode(RAW_PASSWORD);
      log.info("result: {}", encodedPassword);

      boolean matches = passwordEncoder.matches(RAW_PASSWORD, encodedPassword);
      assertThat(matches).isTrue();
    }

  }

}
