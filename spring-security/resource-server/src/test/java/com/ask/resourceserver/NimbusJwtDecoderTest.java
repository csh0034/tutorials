package com.ask.resourceserver;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StreamUtils;

@Slf4j
class NimbusJwtDecoderTest {

  private Properties properties;

  @BeforeEach
  void beforeEach() throws Exception {
    properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("token.properties"));
  }

  @DisplayName("Hmac Decoder 검증")
  @Test
  void withHmac() {
    // given
    String secret = "0123456789-0123456789-0123456789"; // 32자 이상되어야함
    SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

    // when
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();

    // then
    Jwt jwt = jwtDecoder.decode(properties.getProperty("hmacToken"));
    log.info("jwt headers: {}", jwt.getHeaders());
    log.info("jwt claims: {}", jwt.getClaims());
  }

  @DisplayName("RSA Decoder 검증")
  @Test
  void withRsa() throws Exception {
    // given
    ClassPathResource classPathResource = new ClassPathResource("simple.pub");
    String key = StreamUtils.copyToString(classPathResource.getInputStream(), StandardCharsets.UTF_8);

    RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
        .generatePublic(new X509EncodedKeySpec(getKeySpec(key)));

    // when
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();

    // then
    Jwt jwt = jwtDecoder.decode(properties.getProperty("messageReadToken"));
    log.info("jwt headers: {}", jwt.getHeaders());
    log.info("jwt claims: {}", jwt.getClaims());
  }

  private byte[] getKeySpec(String keyValue) {
    keyValue = keyValue.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
    return Base64.getMimeDecoder().decode(keyValue);
  }
}
