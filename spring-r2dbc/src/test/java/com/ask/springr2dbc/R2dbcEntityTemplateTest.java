package com.ask.springr2dbc;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.ask.springr2dbc.config.Transaction;
import com.ask.springr2dbc.model.User;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(Transaction.class)
@Slf4j
public class R2dbcEntityTemplateTest {

  @Autowired
  private R2dbcEntityTemplate template;

  @Test
  void insert() {
    template.insert(User.class)
        .using(User.create("ASk", 29))
        .as(Transaction::withRollback)
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void first() {
    template.select(User.class)
        .first()
        .doOnNext(it -> log.info("{}", it))
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void selectOne() {
    template.insert(User.create("ASk", 29))
        .flatMap(user -> template.selectOne(query(where("name").is("ASk")), User.class))
        .log()
        .as(Transaction::withRollback)
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void selectAll() {
    template.select(User.class)
        .all()
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void complexQueryWithProjection() {
    template.insert(User.create("ASk", 29))
        .flatMap(user -> template.select(UserVO.class)
            .from("mt_user")
            .matching(query(where("name").is("ASk")
                .and("age").between(20, 30))
                .sort(by(desc("id"))))
            .one())
        .log()
        .as(Transaction::withRollback)
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }

  @ToString
  public static class UserVO {

    private String name;
    private Integer age;

  }

}
