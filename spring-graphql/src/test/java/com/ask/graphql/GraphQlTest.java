package com.ask.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureGraphQlTester
@Transactional
class GraphQlTest {

  @Autowired
  private GraphQlTester graphQlTester;

  /**
   * {
   *   "data": {
   *     "save": {
   *       "id": "cfbeff15-5858-443a-8fc8-6e39ebdca735",
   *       "name": "sample-food"
   *     }
   *   }
   * }
   */
  @Test
  void save() {
    graphQlTester.documentName("food")
        .operationName("save")
        .variable("name", "sample-food")
        .execute()
        .path("save.name")
        .entity(String.class)
        .isEqualTo("sample-food");
  }

  /**
   * {
   *   "data": {
   *     "findFood": {
   *       "id": "food01",
   *       "name": "name01"
   *     }
   *   }
   * }
   */
  @Test
  void findFood() {
    graphQlTester.documentName("food")
        .operationName("findFood")
        .variable("name", "name01")
        .execute()
        .path("findFood.id")
        .entity(String.class)
        .isEqualTo("food01");
  }

  /**
   * {
   *   "data": {
   *     "findFoods": [
   *       {
   *         "id": "food01",
   *         "name": "name01"
   *       },
   *       {
   *         "id": "food02",
   *         "name": "name02"
   *       }
   *     ]
   *   }
   * }
   */
  @Test
  void findFoods() {
    graphQlTester.documentName("food")
        .operationName("findFoods")
        .execute()
        .path("findFoods[*].id")
        .entityList(String.class)
        .containsExactly("food01", "food02");
  }

}
