package com.ask.springr2dbc.web;

import com.ask.springr2dbc.model.User;
import com.ask.springr2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @GetMapping
  public Flux<User> get() {
    return userService.findAll();
  }

  @PostMapping
  public Mono<User> post(@RequestBody User user) {
    log.info("POST user : {}", user);
    return userService.save(user);
  }

  @PutMapping
  public Mono<User> put(@RequestBody User user) {
    log.info("PUT user : {}", user);
    return userService.update(user);
  }

  @DeleteMapping
  public Mono<Void> delete(@RequestParam String id) {
    log.info("DELETE id : {}", id);
    return userService.delete(id);
  }

}
