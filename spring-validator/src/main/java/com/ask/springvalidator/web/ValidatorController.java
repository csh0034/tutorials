package com.ask.springvalidator.web;

import com.ask.springvalidator.vo.ValidatorVO1;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ValidatorController {

  @GetMapping("/validator1")
  public void validator1(@Valid @RequestBody ValidatorVO1 validatorVO1) {
    log.info("vo : {}", validatorVO1);
  }
}
