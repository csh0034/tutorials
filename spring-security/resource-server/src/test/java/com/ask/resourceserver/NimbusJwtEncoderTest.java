package com.ask.resourceserver;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Slf4j
class NimbusJwtEncoderTest {

  @DisplayName("Hmac Encoder 검증")
  @Test
  void withHmac() {
    // given
    String secret = "0123456789-0123456789-0123456789"; // 32자 이상되어야함
    JwtEncoder jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes(StandardCharsets.UTF_8)));

    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
        .type("JWT")
        .build();

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject("Hello Nimbus")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600))
        .build();

    JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(header, claims);

    // when
    Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);

    // then
    log.info("jwt tokenValue: {}", jwt.getTokenValue());
  }

}
