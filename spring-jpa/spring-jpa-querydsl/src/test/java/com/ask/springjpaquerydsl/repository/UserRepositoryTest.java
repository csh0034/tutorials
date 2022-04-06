package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QUser.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.ask.springjpaquerydsl.entity.Company;
import com.ask.springjpaquerydsl.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

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

  @DisplayName("paging 처리")
  @Test
  void paging() {
    // given
    Pageable pageable = PageRequest.of(3, 2);

    // when
    List<User> users = queryFactory.selectFrom(user)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory.select(user.count())
        .from(user)
        .fetchOne();

    // then
    Page<User> page = new PageImpl<>(users, pageable, total);

    log.info("content: {}", page.getContent());
    log.info("size: {}", page.getSize());
    log.info("totalElements: {}", page.getTotalElements());
    log.info("totalPages: {}", page.getTotalPages());
    log.info("number: {}", page.getNumber());
  }

}
