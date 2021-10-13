package com.ask.springjparedisson.web;

import com.ask.springjparedisson.entity.Company;
import com.ask.springjparedisson.entity.User;
import com.ask.springjparedisson.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/users")
  public User add(@RequestParam(defaultValue = "ask") String name) {
    String id = userService.add(name);
    return userService.find(id);
  }

  @GetMapping("/users/{id}")
  public User find(@PathVariable String id) {
    return userService.find(id);
  }

  @GetMapping("/company")
  public Company find() {
    return userService.getStoredCompany();
  }
}
