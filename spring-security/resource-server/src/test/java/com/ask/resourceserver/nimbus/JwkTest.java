package com.ask.resourceserver.nimbus;

import static org.assertj.core.api.Assertions.assertThat;

import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.IOUtils;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class JwkTest {

  @Test
  void rsaPrivate() throws Exception {
    InputStream is = new ClassPathResource("nimbus/rsa-private.pem").getInputStream();
    String privatePem = IOUtils.readInputStreamToString(is);

    JWK jwk = JWK.parseFromPEMEncodedObjects(privatePem);
    log.info("jwk: {}", jwk);

    assertThat(jwk).isInstanceOf(RSAKey.class);
    assertThat(jwk.isPrivate()).isTrue();
  }

  @Test
  void rsaPublic() throws Exception {
    InputStream is = new ClassPathResource("nimbus/rsa-public.pem").getInputStream();
    String publicPem = IOUtils.readInputStreamToString(is);

    JWK jwk = JWK.parseFromPEMEncodedObjects(publicPem);
    log.info("jwk: {}", jwk);

    assertThat(jwk).isInstanceOf(RSAKey.class);
    assertThat(jwk.isPrivate()).isFalse();
  }

  @Test
  void ecdsaPrivate() throws Exception {
    InputStream is = new ClassPathResource("nimbus/ecdsa-p256-private.pem").getInputStream();
    String privatePem = IOUtils.readInputStreamToString(is);

    JWK jwk = JWK.parseFromPEMEncodedObjects(privatePem);
    log.info("jwk: {}", jwk);

    assertThat(jwk).isInstanceOf(ECKey.class);
    assertThat(jwk.isPrivate()).isTrue();
  }

  @Test
  void ecdsaPublic() throws Exception {
    InputStream is = new ClassPathResource("nimbus/ecdsa-p256-public.pem").getInputStream();
    String publicPem = IOUtils.readInputStreamToString(is);

    JWK jwk = JWK.parseFromPEMEncodedObjects(publicPem);
    log.info("jwk: {}", jwk);

    assertThat(jwk).isInstanceOf(ECKey.class);
    assertThat(jwk.isPrivate()).isFalse();
  }

}
