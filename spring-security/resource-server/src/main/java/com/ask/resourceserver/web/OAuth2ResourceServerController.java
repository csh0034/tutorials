package com.ask.resourceserver.web;

import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OAuth2ResourceServerController {

  /**
   * @param jwt AuthenticationPrincipalArgumentResolver 에서 처리
   * @param principal ServletRequestMethodArgumentResolver 에서 처리
   */
  @GetMapping("/")
  public String index(@AuthenticationPrincipal Jwt jwt, Principal principal) {
    log.info("jwt: {}", jwt);
    log.info("principal: {}", principal);
    log.info("principal.name: {}", principal.getName());
    return String.format("Hello, %s!", jwt.getSubject());
  }

  @GetMapping("/message")
  public String message() {
    return "secret message";
  }

  @PostMapping("/message")
  public String createMessage(@RequestBody String message) {
    return String.format("Message was created. Content: %s", message);
  }
}
