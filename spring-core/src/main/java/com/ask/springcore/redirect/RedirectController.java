package com.ask.springcore.redirect;

import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

  @GetMapping("/redirect")
  public String redirect() {
    return "redirect:/redirected";
  }

  @GetMapping("/redirect2")
  public ResponseEntity<Resource> redirect2() {
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create("/redirected"))
        .build();
  }

  @GetMapping("/notFound")
  public String notFound(HttpServletResponse response) throws IOException {
    response.sendError(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

}
