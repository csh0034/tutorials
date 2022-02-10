package com.ask.gateway.filter;

import com.ask.gateway.config.security.JwtUtils;
import com.ask.gateway.config.security.TokenUser;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

  private static final String VERIFICATION_TARGET_PATH = "/jwt";

  private final JwtUtils jwtUtils;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();

    if (!matchesPath(request)) {
      return chain.filter(exchange);
    }

    if (!containsAuthorization(request)) {
      return onError(response, "Authorization header is missing in request");
    }

    String token = extractToken(request);
    if (!jwtUtils.isValid(token)) {
      return onError(response, "Authorization header is not valid");
    }

    addAuthorizationHeaders(request, token);

    return chain.filter(exchange);
  }

  private boolean matchesPath(ServerHttpRequest request) {
    return request.getURI().getPath().startsWith(VERIFICATION_TARGET_PATH);
  }

  private boolean containsAuthorization(ServerHttpRequest request) {
    return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
  }

  private Mono<Void> onError(ServerHttpResponse response, String message) {
    response.setStatusCode(HttpStatus.BAD_REQUEST);
    DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }

  private String extractToken(ServerHttpRequest request) {
    return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
  }

  private void addAuthorizationHeaders(ServerHttpRequest request, String token) {
    TokenUser tokenUser = jwtUtils.decode(token);

    request.mutate()
        .header("X-Authorization-Id", tokenUser.getId())
        .header("X-Authorization-Role", tokenUser.getRole())
        .build();
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

}
