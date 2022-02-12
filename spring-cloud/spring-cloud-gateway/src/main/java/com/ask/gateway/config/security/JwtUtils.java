package com.ask.gateway.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils implements InitializingBean {

  private static final String ROLE_CLAIM_KEY = "role";

  private final JwtProperties jwtProperties;

  private Algorithm algorithm;
  private JWTVerifier jwtVerifier;

  @Override
  public void afterPropertiesSet() {
    this.algorithm = Algorithm.HMAC512(jwtProperties.getSecret());
    this.jwtVerifier = JWT.require(algorithm).acceptLeeway(5).build();
  }

  public boolean isValid(String token) {
    try {
      jwtVerifier.verify(token);
      return true;
    } catch (RuntimeException e){
      return false;
    }
  }

  public TokenUser decode(String token) {
    jwtVerifier.verify(token);

    DecodedJWT jwt = JWT.decode(token);

    String id = jwt.getSubject();
    String role = jwt.getClaim(ROLE_CLAIM_KEY).asString();

    return new TokenUser(id, role);
  }

  String generate(TokenUser user) {
    Date now = new Date();
    Date expiresAt = new Date(now.getTime() + jwtProperties.getExpirationSecond() * 1000);

    return JWT.create()
        .withSubject(user.getId())
        .withClaim(ROLE_CLAIM_KEY, user.getRole())
        .withExpiresAt(expiresAt)
        .withIssuedAt(now)
        .sign(algorithm);
  }

}
