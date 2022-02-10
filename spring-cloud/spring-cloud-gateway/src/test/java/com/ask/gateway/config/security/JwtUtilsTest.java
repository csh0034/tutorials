package com.ask.gateway.config.security;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class JwtUtilsTest {

  private JwtUtils jwtUtils;

  @BeforeEach
  void setUp() {
    JwtProperties jwtProperties = new JwtProperties();
    jwtProperties.setSecret("secret");
    jwtProperties.setExpirationSecond(3600 * 6);

    jwtUtils = new JwtUtils(jwtProperties);
    jwtUtils.afterPropertiesSet();
  }

  @Test
  void decode() {
    // given
    TokenUser tokenUser = new TokenUser("ASk", "ROLE_USER");
    String generatedToken = jwtUtils.generate(tokenUser);

    // when
    TokenUser decodedUser = jwtUtils.decode(generatedToken);

    // then
    log.info("generatedToken : {}", generatedToken);
    assertThat(decodedUser).isEqualTo(tokenUser);
  }

}
