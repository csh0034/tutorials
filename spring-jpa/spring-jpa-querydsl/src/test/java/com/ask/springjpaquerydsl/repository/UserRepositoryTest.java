package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.ask.springjpaquerydsl.entity.Company;
import com.ask.springjpaquerydsl.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
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
  private CompanyRepository companyRepository;

  @Autowired
  private JPAQueryFactory queryFactory;

  @DisplayName("user 저장")
  @Test
  void select() {
    // given
    Company company = Company.create("company1");
    companyRepository.save(company);

    User createUser = User.create("abc123", "1234", company);
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
