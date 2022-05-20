package com.ask.resourceserver.nimbus;

import static org.assertj.core.api.Assertions.assertThat;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.ECKey.Builder;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ECDSATest {

  private static final ECKey ecKey;

  static {
    try {
      ecKey = new ECKeyGenerator(Curve.P_256)
          .keyID("123")
          .generate();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void printKey() {
    log.info("public key(x): {}", ecKey.getX());
    log.info("public key(y): {}", ecKey.getY());
    log.info("private key(d): {}", ecKey.getD());
  }


  @Test
  void generateAndVerify() throws Exception {
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
        .type(JOSEObjectType.JWT)
        .keyID(ecKey.getKeyID())
        .build();

    JWTClaimsSet payload = new JWTClaimsSet.Builder()
        .subject("Hello Nimbus")
        .issueTime(new Date())
        .build();

    ECKey publicKey = new ECKey.Builder(Curve.P_256, ecKey.getX(), ecKey.getY())
        .build();

    ECKey privateKey = new ECKey.Builder(Curve.P_256, ecKey.getX(), ecKey.getY())
        .d(ecKey.getD())
        .build();

    SignedJWT signedJWT = new SignedJWT(header, payload);
    signedJWT.sign(new ECDSASigner(privateKey));

    String jwt = signedJWT.serialize();
    log.info("jwt: {}", jwt);

    assertThat(jwt).isNotNull();
    assertThat(SignedJWT.parse(jwt).verify(new ECDSAVerifier(publicKey))).isTrue();
  }

}
