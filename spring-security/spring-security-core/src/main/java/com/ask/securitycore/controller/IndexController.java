package com.ask.securitycore.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

  private final HttpSession session;

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @ResponseBody
  @GetMapping("/main")
  public Map<String, Object> main(Principal principal) {
    Map<String, Object> response = createResponse();
    response.put("principal", principal);
    return response;
  }

  @GetMapping("/main/login")
  public String mainLogin() {
    return "login";
  }

  @GetMapping("/main/view")
  public String mainView() {
    return "view";
  }

  @ResponseBody
  @GetMapping("/sub")
  public Map<String, Object> sub(Principal principal) {
    Map<String, Object> response = createResponse();
    response.put("principal", principal);
    return response;
  }

  @GetMapping("/sub/login")
  public String subLogin() {
    return "login";
  }

  @GetMapping("/sub/view")
  public String subView() {
    return "view";
  }

  private Map<String, Object> createResponse() {
    Map<String, Object> res = new LinkedHashMap<>();
    res.put("sessionId", session.getId());
    res.put("SPRING_SECURITY_CONTEXT", session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
    return res;
  }
}
