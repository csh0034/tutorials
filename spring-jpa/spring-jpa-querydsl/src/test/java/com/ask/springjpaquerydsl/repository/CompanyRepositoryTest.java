package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QCompany.company;
import static com.ask.springjpaquerydsl.entity.QUser.user;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@Slf4j
class CompanyRepositoryTest {

  @Autowired
  private JPAQueryFactory queryFactory;

  @Test
  void collectionJoin() {
    List<UserDto> users = queryFactory.select(Projections.constructor(UserDto.class,
            company.id,
            company.name,
            user.id,
            user.name))
        .from(company)
        .leftJoin(company.users, user)
        .fetch();

    users.forEach(userDto -> log.info("userDto : {}", userDto));
  }

  @AllArgsConstructor
  @ToString
  public static class UserDto {

    private String companyId;
    private String companyName;
    private String userId;
    private String userName;
  }
}
