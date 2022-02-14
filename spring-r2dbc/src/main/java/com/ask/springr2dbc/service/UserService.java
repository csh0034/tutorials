package com.ask.springr2dbc.service;

import com.ask.springr2dbc.model.User;
import com.ask.springr2dbc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Flux<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional
  public Mono<User> save(User requestUser) {
    Assert.isNull(requestUser.getId(), "userId must be null");
    return userRepository.save(requestUser);
  }

  @Transactional
  public Mono<User> update(User requestUser) {
    Assert.notNull(requestUser.getId(), "userId must not be null");

    return userRepository.findById(requestUser.getId())
        .flatMap(dbUser -> {
          dbUser.update(requestUser);
          return userRepository.save(dbUser);
        });
  }

  @Transactional
  public Mono<Void> delete(String id) {
    return userRepository.findById(id)
        .flatMap(userRepository::delete);
  }

}
