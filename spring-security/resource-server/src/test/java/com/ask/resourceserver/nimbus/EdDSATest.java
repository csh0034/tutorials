package com.ask.resourceserver.nimbus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Ed25519는 SHA-512 및 Curve25519를 사용한 EdDSA 서명 체계이다. <br> 공개키, 개인키가 각 32byte 이며 base64url 로 encode 한다.
 */
@Slf4j
class EdDSATest {

  private static final OctetKeyPair keyPair;

  static {
    try {
      keyPair = new OctetKeyPairGenerator(Curve.Ed25519)
          .keyID("123")
          .generate();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void printKey() {
    log.info("public key(x): {}", keyPair.getX());
    log.info("private key(d): {}", keyPair.getD());
  }

  @Test
  void printJwk() {
    log.info("jwk: {}", keyPair.toString());
  }

  @Test
  void buildPublicKey() {
    String x = "0123456789-0123456789-0123456789";
    OctetKeyPair publicKey = new OctetKeyPair.Builder(Curve.Ed25519, Base64URL.encode(x))
        .build();

    assertThat(publicKey.getX().decodeToString()).isEqualTo(x);
  }

  @Test
  void buildPrivateKey() {
    // publicKey 세팅 없이 객체 생성 할 수 없으므로 임의로 지정해놓음
    String d = "0123456789-0123456789-0123456789";
    OctetKeyPair privateKey = new OctetKeyPair.Builder(Curve.Ed25519, Base64URL.encode("not-given"))
        .d(Base64URL.encode(d))
        .build();

    assertThat(privateKey.getD().decodeToString()).isEqualTo(d);
  }

  @Test
  void publicKeyJwk() throws ParseException {
    String jwk = "{\"kty\":\"OKP\",\"crv\":\"Ed25519\"," +
        "\"x\":\"11qYAYKxCrfVS_7TyWQHOg7hcvPapiMlrwIaaPcHURo\"}";

    OctetKeyPair okp = OctetKeyPair.parse(jwk);

    assertThat(okp.getKeyType()).isEqualTo(KeyType.OKP);
    assertThat(okp.getCurve()).isEqualTo(Curve.Ed25519);
    assertThat(okp.getX()).isEqualTo(Base64URL.from("11qYAYKxCrfVS_7TyWQHOg7hcvPapiMlrwIaaPcHURo"));
    assertThat(okp.isPrivate()).isFalse();
  }

  @Test
  void privateKeyJwk() throws ParseException {
    String jwk = "{\"kty\":\"OKP\",\"crv\":\"Ed25519\"," +
        "\"d\":\"nWGxne_9WmC6hEr0kuwsxERJxWl7MmkZcDusAxyuf2A\"," +
        "\"x\":\"11qYAYKxCrfVS_7TyWQHOg7hcvPapiMlrwIaaPcHURo\"}";

    OctetKeyPair okp = OctetKeyPair.parse(jwk);

    assertThat(okp.getKeyType()).isEqualTo(KeyType.OKP);
    assertThat(okp.getCurve()).isEqualTo(Curve.Ed25519);
    assertThat(okp.getX()).isEqualTo(Base64URL.from("11qYAYKxCrfVS_7TyWQHOg7hcvPapiMlrwIaaPcHURo"));
    assertThat(okp.getD()).isEqualTo(Base64URL.from("nWGxne_9WmC6hEr0kuwsxERJxWl7MmkZcDusAxyuf2A"));
    assertThat(okp.isPrivate()).isTrue();

    OctetKeyPair pubJWK = okp.toPublicJWK();

    assertThat(pubJWK.getKeyType()).isEqualTo(KeyType.OKP);
    assertThat(pubJWK.getCurve()).isEqualTo(Curve.Ed25519);
    assertThat(pubJWK.getX()).isEqualTo(okp.getX());
    assertThat(pubJWK.getD()).isNull();
    assertThat(pubJWK.isPrivate()).isFalse();
  }

  @DisplayName("Ed25519Verifier 는 public key 가 32 byte 가 아닐 경우 예외 발생 ")
  @Test
  void ed25519Verifier() {
    String x = "not-32-byte";
    OctetKeyPair publicKey = new OctetKeyPair.Builder(Curve.Ed25519, Base64URL.encode(x))
        .build();

    assertThatIllegalArgumentException().isThrownBy(() -> new Ed25519Verifier(publicKey))
        .withMessageContaining("Given public key's length is not 32.");
  }

  @Test
  void generateAndVerify() throws Exception {
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
        .type(JOSEObjectType.JWT)
        .keyID(keyPair.getKeyID())
        .build();

    JWTClaimsSet payload = new JWTClaimsSet.Builder()
        .subject("Hello Nimbus")
        .issueTime(new Date())
        .build();

    OctetKeyPair publicKey = new OctetKeyPair.Builder(Curve.Ed25519, keyPair.getX())
        .build();

    OctetKeyPair privateKey = new OctetKeyPair.Builder(Curve.Ed25519, Base64URL.encode("not-given"))
        .d(keyPair.getD())
        .build();

    SignedJWT signedJWT = new SignedJWT(header, payload);
    signedJWT.sign(new Ed25519Signer(privateKey));

    String jwt = signedJWT.serialize();
    log.info("jwt: {}", jwt);

    assertThat(jwt).isNotNull();
    assertThat(SignedJWT.parse(jwt).verify(new Ed25519Verifier(publicKey))).isTrue();
  }

  @DisplayName("jwt format 이 아닐 경우 예외 검증")
  @Test
  void verifyWithInvalidFormatKey() {
    assertThatExceptionOfType(ParseException.class)
        .isThrownBy(() -> {
          SignedJWT.parse("invalid-format");
        });
  }

  @DisplayName("다른 PublicKey 로 검증할 경우 false")
  @Test
  void verifyWithInvalidPublicKey() throws Exception {
    String x = "0123456789-0123456789-0123456789"; // 32byte 여야 함.
    OctetKeyPair publicKey = new OctetKeyPair.Builder(Curve.Ed25519, Base64URL.encode(x))
        .build();

    SignedJWT signedJWT = SignedJWT.parse(
        "eyJraWQiOiIxMjMiLCJ0eXAiOiJKV1QiLCJhbGciOiJFZERTQSJ9.eyJzdWIiOiJIZWxsbyBOaW1idXMiLCJpYXQiOjE2NTI4NDg5OTN9.6riChwDae9F401-P3RNtmIaEit2U6goOzQx-QpbsiFeF5NZ-v8A7wC8JlgoezzCaHqa1UtpFsAKBnyOTir6jBQ");

    assertThat(signedJWT.verify(new Ed25519Verifier(publicKey))).isFalse();
  }

  @DisplayName("jwks 생성")
  @Test
  void jwks() {
    JWKSet jwkSet = new JWKSet(keyPair);
    log.info("jwk: {}", jwkSet);
  }

}
