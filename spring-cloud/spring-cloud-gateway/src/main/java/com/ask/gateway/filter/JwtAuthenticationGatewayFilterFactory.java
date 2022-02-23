package com.ask.gateway.filter;

import com.ask.gateway.config.security.JwtUtils;
import com.ask.gateway.config.security.TokenUser;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthenticationGatewayFilterFactory extends
    AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

  private static final String ROLE_KEY = "role";

  private final JwtUtils jwtUtils;

  public JwtAuthenticationGatewayFilterFactory(JwtUtils jwtUtils) {
    super(Config.class);
    this.jwtUtils = jwtUtils;
  }

  @Override
  public List<String> shortcutFieldOrder() {
    return Collections.singletonList(ROLE_KEY);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();

      if (!containsAuthorization(request)) {
        return onError(response, "missing authorization header", HttpStatus.BAD_REQUEST);
      }

      String token = extractToken(request);
      if (!jwtUtils.isValid(token)) {
        return onError(response, "invalid authorization header", HttpStatus.BAD_REQUEST);
      }

      TokenUser tokenUser = jwtUtils.decode(token);
      if (!hasRole(tokenUser, config.role)) {
        return onError(response, "invalid role", HttpStatus.FORBIDDEN);
      }

      addAuthorizationHeaders(request, tokenUser);

      return chain.filter(exchange);
    };
  }

  private boolean containsAuthorization(ServerHttpRequest request) {
    return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
  }

  private String extractToken(ServerHttpRequest request) {
    return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
  }

  private boolean hasRole(TokenUser tokenUser, String role) {
    return role.equals(tokenUser.getRole());
  }

  private void addAuthorizationHeaders(ServerHttpRequest request, TokenUser tokenUser) {
    request.mutate()
        .header("X-Authorization-Id", tokenUser.getId())
        .header("X-Authorization-Role", tokenUser.getRole())
        .build();
  }

  private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
    response.setStatusCode(status);
    response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
    DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }

  @Setter
  public static class Config {

    private String role;

  }

}

