package com.ask.multitenancy.controller;

import com.ask.multitenancy.entity.tenant.User;
import com.ask.multitenancy.repository.tenant.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TenantController {

  private final UserRepository userRepository;

  @GetMapping("/users")
  public ResponseEntity<List<User>> findAll() {
    List<User> users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }
}
