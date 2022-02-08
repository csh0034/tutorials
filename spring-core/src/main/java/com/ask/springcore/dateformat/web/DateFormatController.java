package com.ask.springcore.dateformat.web;

import com.ask.springcore.dateformat.vo.TestVO1;
import com.ask.springcore.dateformat.vo.TestVO2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/date-format")
public class DateFormatController {

  @GetMapping("/1")
  public TestVO1 method1(TestVO1 testVO1) {
    return testVO1;
  }

  @GetMapping("/2")
  public TestVO2 method2(TestVO2 testVO2) {
    return testVO2;
  }

  @PostMapping("/3")
  public TestVO1 method3(TestVO1 testVO1) {
    return testVO1;
  }

  @PostMapping("/4")
  public TestVO2 method4(TestVO2 testVO2) {
    return testVO2;
  }

  @PostMapping("/5")
  public TestVO1 method5(@RequestBody TestVO1 testVO1) {
    return testVO1;
  }

  @PostMapping("/6")
  public TestVO2 method6(@RequestBody TestVO2 testVO2) {
    return testVO2;
  }

}
