package com.ask.authorizationserver.config;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:rsa/key.properties")
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String RSA_ALGORITHM = "RSA";

  private final Environment env;

  @Bean
  public RSAPublicKey rsaPublicKey() {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      byte[] bytePublicKey = Base64.getDecoder().decode(Objects.requireNonNull(env.getProperty("public")).getBytes());
      return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Bean
  public RSAPrivateKey rsaPrivateKey() {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
      byte[] bytePublicKey = Base64.getDecoder().decode(Objects.requireNonNull(env.getProperty("private")).getBytes());
      return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytePublicKey));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
