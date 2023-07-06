package com.ask.resourceserver.nimbus;

import static org.assertj.core.api.Assertions.assertThat;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import java.net.URL;
import org.junit.jupiter.api.Test;

class RsaTest {

  @Test
  void jwkVerify() throws Exception {
    JWKSet jwks = JWKSet.load(new URL("https://appleid.apple.com/auth/keys"));
    RSAKey rsaKey = jwks.getKeyByKeyId("fh6Bs8C").toRSAKey();

    JWSVerifier verifier = new RSASSAVerifier(rsaKey);

    boolean verify = SignedJWT.parse("eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJtZXNzYWdpbmctY2xpZW50IiwiYXVkIjoibWVzc2FnaW5nLWNsaWVudCIsIm5iZiI6MTY1Njg1MzEyOCwic2NvcGUiOlsibWVzc2FnZTpyZWFkIl0sImlzcyI6Imh0dHA6XC9cL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNjU2ODUzMTI5LCJpYXQiOjE2NTY4NTMxMjh9.w48jhw_APFBnSZ038-AfiQrG4GduJpxKwpCMiKOagPauj-zml1OI1UaAASzVL0nHmh2rdLe46Uou1FIRtCr0JUoFT2zZut8UJvazCvS9bOyh7yRG900cXjIp-ZuIXsVz0z_ROrUijmJRAnKf3o9PogIg2vmV9zoRDliWDfeMAazICU-NNMsLAUyFXMVO6YjBYHH6oQw9l7vc8z0LDuVuV4evFNCh36f2YI_ljO4UTta6Xf5mZxSshbt7tBU8-EwvoCfqLCpoEj3r8RZ9h-QEUQ1fSpWWCUiTPE_HXdHzlI4DXFryvRE5khZ3uF1_V_bNShgU0FxjmQ46tnuwJOF4Og").verify(verifier);
    assertThat(verify).isFalse();
  }

}
