package com.ask.springjpacore;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpacore.entity.Password;
import com.ask.springjpacore.entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityManagerFactoryTest {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Test
  void transaction() {
    // given
    User user = User.create("ask", Password.create("1234"));
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // when
    entityManager.getTransaction().begin();
    entityManager.persist(user);
    entityManager.getTransaction().commit();

    // then
    List<User> users = entityManager.createQuery("select u from User u", User.class).getResultList();
    assertThat(users).hasSize(1);
  }

}
