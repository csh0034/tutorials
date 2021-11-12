package com.ask.springcore.session;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SessionController {

  public static final String TEST_SESSION_KEY = "session-key";

  private final HttpSession session;

  @GetMapping("/save/session")
  public String saveSession() {
    log.info("session id : {}", session.getId());
    session.setAttribute(TEST_SESSION_KEY, System.currentTimeMillis());
    return "success!";
  }
}
