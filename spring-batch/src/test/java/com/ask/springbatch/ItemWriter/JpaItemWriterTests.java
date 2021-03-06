package com.ask.springbatch.ItemWriter;

import static java.util.stream.Collectors.toList;

import com.ask.springbatch.entity.User;
import com.ask.springbatch.entity.UserNotAuto;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JpaItemWriterTests {
  
  private static final int USER_SIZE = 1000;

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @DisplayName("id 직접 생성 할때")
  @Nested
  class Class1 {

    List<UserNotAuto> items;

    @BeforeEach
    void setup() {
      items = IntStream.iterate(1, i -> i + 1)
          .limit(USER_SIZE)
          .mapToObj(i -> UserNotAuto.create(UUID.randomUUID().toString(), "name" + i, "password" + i, true))
          .collect(toList());
    }

    @DisplayName("id 값이 있고 merge 호출하면 insert, select")
    @Test
    void itemWriter1() {
      // GIVEN
      JpaItemWriter<UserNotAuto> writer = new JpaItemWriterBuilder<UserNotAuto>()
          .entityManagerFactory(entityManagerFactory)
          .build();

      // WHEN
      writer.write(items);
    }

    @DisplayName("id 값이 있고 persist 호출하면 insert")
    @Test
    void itemWriter2() {
      // GIVEN
      JpaItemWriter<UserNotAuto> writer = new JpaItemWriterBuilder<UserNotAuto>()
          .entityManagerFactory(entityManagerFactory)
          .usePersist(true)
          .build();

      // WHEN
      writer.write(items);
    }
  }

  @DisplayName("id GenericGenerator 로 생성 할때")
  @Nested
  class Class2 {

    List<User> items;

    @BeforeEach
    void setup() {
      items = IntStream.iterate(1, i -> i + 1)
          .limit(USER_SIZE)
          .mapToObj(i -> User.create("name" + i, "password" + i, true))
          .collect(toList());
    }

    @DisplayName("id 값이 없고 merge 호출하면 insert")
    @Test
    void itemWriter1() {
      // GIVEN
      JpaItemWriter<User> writer = new JpaItemWriterBuilder<User>()
          .entityManagerFactory(entityManagerFactory)
          .build();

      // WHEN
      writer.write(items);
    }

    @DisplayName("id 값이 없고 persist 호출하면 insert")
    @Test
    void itemWriter2() {
      // GIVEN
      JpaItemWriter<User> writer = new JpaItemWriterBuilder<User>()
          .entityManagerFactory(entityManagerFactory)
          .usePersist(true)
          .build();

      // WHEN
      writer.write(items);
    }

    @DisplayName("id 값이 있고 merge 호출하면 select, insert")
    @Test
    void itemWriter3() {
      // GIVEN
      items = IntStream.iterate(1, i -> i + 1)
          .limit(USER_SIZE)
          .mapToObj(i -> {
            User user = User.create("name" + i, "password" + i, true);
            user.setId("id" + i);
            return user;
          })
          .collect(toList());

      JpaItemWriter<User> writer = new JpaItemWriterBuilder<User>()
          .entityManagerFactory(entityManagerFactory)
          .build();

      // WHEN
      writer.write(items);
    }

    @DisplayName("id 값이 있고 persist 호출하면 Exception")
    @Test
    void itemWriter4() {
      items = IntStream.iterate(1, i -> i + 1)
          .limit(USER_SIZE)
          .mapToObj(i -> {
            User user = User.create("name" + i, "password" + i, true);
            user.setId("id" + i);
            return user;
          })
          .collect(toList());

      // GIVEN
      JpaItemWriter<User> writer = new JpaItemWriterBuilder<User>()
          .entityManagerFactory(entityManagerFactory)
          .usePersist(true)
          .build();

      // WHEN
      Assertions.assertThrows(PersistenceException.class, () -> writer.write(items));
    }
  }
}
