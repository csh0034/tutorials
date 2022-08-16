package com.ask.springjpaquerydsl;

import static com.ask.springjpaquerydsl.entity.QCompany.company;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JPAClauseTest {

  @Autowired
  private JPAQueryFactory queryFactory;

  @Nested
  class JPAUpdateClauseTest {

    @Test
    void update() {
      queryFactory.update(company)
          .where(company.name.eq("name1"))
          .set(company.name, "update-name1")
          .execute();
    }

  }

  @Nested
  class JPADeleteClauseTest {

    @Test
    void delete() {
      // given
      List<String> ids = Arrays.asList("1", "2", "3");

      // expect
      queryFactory.delete(company)
          .where(company.id.in(ids))
          .execute();
    }

  }

}
