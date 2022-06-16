package com.ask.springcore.mapping;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {

  @GetMapping({
      "/mapping",
      "/mapping/{locale:ko|en}"
  })
  public String mapping(@PathVariable(required = false) String locale) {
    return String.format("locale: %s", locale);
  }

  @GetMapping({
      "/mapping2",
      "/mapping2/{locale:ko|en}"
  })
  public String mapping2(@PathVariable Optional<String> locale) {
    return String.format("locale: %s", locale.orElse("default"));
  }

}
