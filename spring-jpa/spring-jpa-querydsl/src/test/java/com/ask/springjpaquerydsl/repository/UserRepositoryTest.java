package com.ask.springjpaquerydsl.repository;

import static com.ask.springjpaquerydsl.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.ask.springjpaquerydsl.config.JpaConfig;
import com.ask.springjpaquerydsl.entity.Company;
import com.ask.springjpaquerydsl.entity.User;
import com.ask.springjpaquerydsl.vo.UserVO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .fetch();

    Long total = queryFactory.select(Wildcard.count) // or user.count()
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

  @DisplayName("paging 처리 clone 이용하여 처리")
  @Test
  void paging2() {
    // given
    Pageable pageable = PageRequest.of(0, 2);

    // when
    JPAQuery<User> query = queryFactory.select(user)
        .from(user)
        .where(user.name.contains("name"));

    Long total = query.clone()
        .select(Wildcard.count)
        .fetchOne();

    List<User> users = query.limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .fetch();

    // then
    Page<User> page = new PageImpl<>(users, pageable, total);

    log.info("content: {}", page.getContent());
    log.info("size: {}", page.getSize());
    log.info("totalElements: {}", page.getTotalElements());
    log.info("totalPages: {}", page.getTotalPages());
    log.info("number: {}", page.getNumber());
  }

  @DisplayName("constant 사용 검증")
  @Test
  void constant() {
    // given
    String constantValue = "constant-value...";

    // when
    List<UserVO> users = queryFactory.select(Projections.fields(UserVO.class,
            user.id,
            user.name,
            Expressions.asString(constantValue).as("constant")
            // Expressions.as(Expressions.constant(constantValue), "constant") 와 결과 동일.
        ))
        .from(user)
        .fetch();

    // that
    users.forEach(userVO -> log.info("userVO: {}", userVO));
  }

  @DisplayName("constant 에 as 없을 경우 예외 발생 검증")
  @Test
  void constantException() {
    // given
    String constantValue = "constant";

    // when then
    assertThatIllegalArgumentException().isThrownBy(() -> Projections.fields(UserVO.class,
        user.id,
        user.name,
        Expressions.constant(constantValue)
    ));
  }

  @DisplayName("querydsl exists 처리")
  @ParameterizedTest
  @CsvSource({
      "user,true",
      "none,false"
  })
  void exists(String keyword, boolean expected) {
    boolean exists = queryFactory.selectOne()
        .from(user)
        .where(user.id.contains(keyword))
        .fetchFirst() != null;  // limit(1).fetchOne() != null;

    assertThat(exists).isEqualTo(expected);
  }

}
