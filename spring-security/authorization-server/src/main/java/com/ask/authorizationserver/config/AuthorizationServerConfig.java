package com.ask.authorizationserver.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthorizationServerConfig {

//  @Value("${rsa.pub}")
//  private RSAPublicKey publicKey;
//
//  @Value("${rsa.priv}")
//  private RSAPrivateKey privateKey;

  @Bean
  @Order(1)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain standardSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests((authorize) -> authorize
            .antMatchers("/favicon.ico").permitAll()
            .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient loginClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("login-client")
        .clientSecret("{noop}openid-connect")
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/login-client")
        .redirectUri("http://127.0.0.1:8080/authorized")
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        .build();

    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("messaging-client")
        .clientSecret("{noop}secret")
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
        .scope("message:read")
        .scope("message:write")
        .build();
    return new InMemoryRegisteredClientRepository(loginClient, registeredClient);
  }

//  @Bean
//  public JWKSource<SecurityContext> jwkSource() {
//    RSAKey rsaKey = new RSAKey.Builder(publicKey)
//        .privateKey(privateKey)
//        //.keyID(UUID.randomUUID().toString())
//        .build();
//    JWKSet jwkSet = new JWKSet(rsaKey);
//
//    return new ImmutableJWKSet<>(jwkSet);
//  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    return new ImmutableSecret<>("0123456789-0123456789-0123456789".getBytes(StandardCharsets.UTF_8));
  }

  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
    return context -> context.getHeaders()
        .algorithm(MacAlgorithm.HS256)
        .type("JWT");
  }

  @Bean
  public ProviderSettings providerSettings() {
    return ProviderSettings.builder()
        .issuer("http://localhost:9000")
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
        User.withUsername("user")
            .password("{noop}password")
            .roles("USER")
            .build());
  }

}
