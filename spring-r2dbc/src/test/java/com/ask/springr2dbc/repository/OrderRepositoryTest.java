package com.ask.springr2dbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ask.springr2dbc.config.Transaction;
import com.ask.springr2dbc.model.Order;
import com.ask.springr2dbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(Transaction.class)
@Slf4j
class OrderRepositoryTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void findAll() {
    orderRepository.findAll()
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void save() {
    // given
    User user = User.create("ASk", 29);
    String orderName = "order...";

    // when then
    userRepository.save(user)
        .mapNotNull(User::getId)
        .flatMap(userId -> orderRepository.save(Order.create(orderName, userId)))
        .as(Transaction::withRollback)
        .as(StepVerifier::create)
        .assertNext(order -> {
          log.info("order : {}", order);

          assertAll(
              () -> assertThat(order.getName()).isEqualTo(orderName),
              () -> assertThat(order.getId()).isNotNull()
          );
        })
        .verifyComplete();
  }

  @Test
  void findAllOrderVO() {
    // given
    User user = User.create("ASk", 29);

    // when then
    userRepository.save(user)
        .mapNotNull(User::getId)
        .flatMap(userId -> orderRepository.save(Order.create("order1", userId)))
        .flatMapMany(order -> orderRepository.findAllOrderVO())
        .as(Transaction::withRollback)
        .as(StepVerifier::create)
        .assertNext(orderVO -> {
          log.info("orderVO : {}", orderVO);
          assertThat(orderVO).isNotNull();
        })
        .verifyComplete();
  }

}
