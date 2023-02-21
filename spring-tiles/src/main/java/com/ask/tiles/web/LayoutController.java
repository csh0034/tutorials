package com.ask.tiles.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LayoutController {

  @GetMapping("/admin/home")
  public String home() {
    return "admin/home.tiles";
  }

}
