package com.ask.springdbcp2.web;

import com.ask.springdbcp2.domain.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeakController {

  private final EntityManagerFactory entityManagerFactory;

  @GetMapping("/leak")
  public List<User> leak() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    return entityManager.createQuery("select user from User user", User.class).getResultList();
  }

}
