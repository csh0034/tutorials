package com.ask.springjpaquerydsl;

import static com.ask.springjpaquerydsl.entity.QCompany.company;
import static com.ask.springjpaquerydsl.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class TransformTest {

  @Autowired
  private JPAQueryFactory queryFactory;

  @Test
  void transform() {
    Map<String, List<String>> map = queryFactory.from(user)
        .join(user.company, company)
        .where(user.id.in("user06", "user07"))
        .transform(groupBy(company.id).as(list(user.id)));

    log.info("{}", map);
  }

}
