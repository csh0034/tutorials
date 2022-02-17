package com.ask.springr2dbc.web;

import com.ask.springr2dbc.model.User;
import com.ask.springr2dbc.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll().subscribe();
  }

  @Test
  void get() {
    // given
    User user = User.create("ASk", 29);
    userRepository.save(user).subscribe();

    // when then
    webTestClient.get()
        .uri("/users")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(User.class)
        .hasSize(1)
        .contains(user)
        .consumeWith(System.out::println);
  }

  /**
   * Security 인증 처리 방법 <br>
   * - @WithMockUser(username = "ASk@test.com") <br>
   * - webTestClient.mutateWith(mockUser("ASk@test.com")).post()
   */
  @Test
  @WithMockUser(username = "ASk@test.com")
  void post() {
    // given
    String name = "ASk";
    int age = 29;

    User user = User.create(name, age);

    // when then
    webTestClient.post()
        .uri("/users")
        .body(BodyInserters.fromValue(user))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.name").isEqualTo(name)
        .jsonPath("$.age").isEqualTo(age)
        .consumeWith(System.out::println);
  }

  @Test
  void put() {
    // given 1
    User user = User.create("ASk", 29);
    userRepository.save(user).subscribe();

    // given 2
    String updatedName = "CSH";
    int updatedAge = 50;

    User updatedUser = User.create(updatedName, updatedAge);
    updatedUser.setId(user.getId());

    // when then
    webTestClient.put()
        .uri("/users")
        .body(BodyInserters.fromValue(updatedUser))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.name").isEqualTo(updatedName)
        .jsonPath("$.age").isEqualTo(updatedAge)
        .consumeWith(System.out::println);
  }

  @Test
  void delete() {
    // given
    User user = User.create("ASk", 29);
    userRepository.save(user).subscribe();

    // when then
    webTestClient.delete()
        .uri(uri -> uri
            .path("/users")
            .queryParam("id", user.getId())
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .consumeWith(System.out::println)
        .isEmpty();
  }

}
