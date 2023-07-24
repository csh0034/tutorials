package com.ask.resourceserver.nimbus;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class DefaultJWTProcessorTest {

  @Test
  void verify() throws Exception {
    String accessToken =
        "eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhbGljZSIsI" +
            "nNjcCI6WyJvcGVuaWQiLCJlbWFpbCJdLCJjbG0iOlsiIUJnIl0sImlzcyI6Imh0dHBzOi8vZGVtby5jM" +
            "mlkLmNvbSIsImV4cCI6MTY5MDA0MDc1OCwiaWF0IjoxNjkwMDQwMTU4LCJ1aXAiOnsiZ3JvdXBzIjpbI" +
            "mFkbWluIiwiYXVkaXQiXX0sImp0aSI6InNjMmRodXRNRzFBIiwiY2lkIjoiMDAwMTIzIn0.QmLMSn4pn" +
            "wGwc04kbhr-CFLHnd4BcDBAtpNLVbf3EymSyRLGcAL3wgdE-V2tMHWO1r2Q8feAr2H8R4AUrkRx2eiWT" +
            "zrTxGLU_T1GVQ2s7nzN7BzLnKxo8y9tArUypq_25rBNNkES6IF2Mu2FwBA8eyoWodQV7xl5bmOBDuZ4l" +
            "09HCdE9sz8PAMt6itQAv-nPsebUAL9vB5r2j8uB_84Uwa2RxtpEYrH36uYryPaW5lQbdkaFm8RA_Dd-t" +
            "PsP5a5yqeQLXOHif1UYYK6S7oEETsmzP7IZyiEaJ5noYesuvmnDHS352ffSGW0hQC84wDJE85gl-jn4l" +
            "d-5DmI3dWeS9A";

    // Create a JWT processor for the access tokens
    ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

    // Set the required "typ" header "at+jwt" for access tokens
    jwtProcessor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType("at+jwt")));

    // The public RSA keys to validate the signatures will be sourced from the
    // OAuth 2.0 server's JWK set URL. The key source will cache the retrieved
    // keys for 5 minutes. 30 seconds prior to the cache's expiration the JWK
    // set will be refreshed from the URL on a separate dedicated thread.
    // Retrial is added to mitigate transient network errors.
    JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(new URL("https://demo.c2id.com/jwks.json"));

    // The expected JWS algorithm of the access tokens (agreed out-of-band)
    JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;

    // Configure the JWT processor with a key selector to feed matching public
    // RSA keys sourced from the JWK set URL
    JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
        expectedJWSAlg,
        keySource);
    jwtProcessor.setJWSKeySelector(keySelector);

    // Set the required JWT claims for access tokens
    jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
        new JWTClaimsSet.Builder().issuer("https://demo.c2id.com").build(),
        new HashSet<>(Arrays.asList(
            JWTClaimNames.SUBJECT,
            JWTClaimNames.ISSUED_AT,
            JWTClaimNames.EXPIRATION_TIME,
            "scp",
            "cid",
            JWTClaimNames.JWT_ID))));

    // Process the token
    SecurityContext ctx = null; // optional context parameter, not required here
    JWTClaimsSet claimsSet;
    try {
      claimsSet = jwtProcessor.process(accessToken, ctx);
    } catch (ParseException | BadJOSEException e) {
      // Invalid token
      log.debug(e.getMessage());
      return;
    } catch (JOSEException e) {
      // Key sourcing failed or another internal exception
      log.debug(e.getMessage());
      return;
    }

    // Print out the token claims set
    log.debug("{}", claimsSet.toJSONObject());
  }

}
