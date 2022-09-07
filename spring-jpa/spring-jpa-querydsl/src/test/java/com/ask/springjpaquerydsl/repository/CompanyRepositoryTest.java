package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QCompany.company;
import static com.ask.springjpaquerydsl.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.junit.jupiter.api.DisplayName;
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

  @DisplayName("join alias 미지정시에 예외 발생 검증")
  @Test
  void withoutJoinAlias() {
    // 하단과 같이 alias 사용하지 않을 경우 user.id, user.name 부분에서 Invalid path: 'user.id' 예외 발생
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> queryFactory
            .select(Projections.constructor(UserDto.class,
                company.id,
                company.name,
                user.id,
                user.name))
            .from(company)
            .leftJoin(company.users)
            .fetch())
        .withCauseInstanceOf(QuerySyntaxException.class)
        .withMessageContaining("Invalid path");
  }

  @DisplayName("where 절에 null 이 전달되어도 예외 발생 안함")
  @Test
  void whereClause() {
    assertThatNoException().isThrownBy(() -> queryFactory
        .selectFrom(company)
        .where((Predicate) null)
        .fetch());
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
