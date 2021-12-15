package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.ask.springjpaquerydsl.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@Slf4j
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JPAQueryFactory queryFactory;

  @Test
  void select() {
    // given
    User createUser = User.create("abc123", "1234");
    userRepository.save(createUser);

    // when
    User selectUser = queryFactory.selectFrom(user)
        .where(user.name.contains("abc"))
        .fetchFirst();

    // then
    log.info("selectUser : {}", selectUser);
    assertThat(selectUser).isEqualTo(createUser);
  }
}