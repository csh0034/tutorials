package com.ask.oauth2client.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/*")
  public String all() {
    return "all";
  }

}
