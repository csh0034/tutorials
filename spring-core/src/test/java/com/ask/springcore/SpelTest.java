package com.ask.springcore;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
class SpELTest {

  @Test
  void object() {
    // given
    User user = User.of("ASk", 20, Address.from("1234"));

    // when
    ExpressionParser expressionParser = new SpelExpressionParser();
    Expression expression = expressionParser.parseExpression("name");

    EvaluationContext context = new StandardEvaluationContext(user);
    String result = expression.getValue(context, String.class);

    // then
    assertThat(result).isEqualTo("ASk");
  }

  @Test
  void variables() {
    // given
    User user = User.of("ASk", 20, Address.from("1234"));

    // when
    ExpressionParser expressionParser = new SpelExpressionParser();
    Expression expression = expressionParser.parseExpression("#user.address.zipCode");

    EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding()
        .build();
//    EvaluationContext context = new StandardEvaluationContext();
    context.setVariable("user", user);

    String result = expression.getValue(context, String.class);

    // then
    assertThat(result).isEqualTo("1234");
  }

  @Data(staticConstructor = "of")
  private static class User {

    private final String name;
    private final int age;
    private final Address address;

  }

  @Data(staticConstructor = "from")
  private static class Address {

    private final String zipCode;

  }

}
