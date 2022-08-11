package com.ask.springcore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
class SpELTest {

  @Nested
  class SpelExpressionParserTest {

    @Test
    void random() {
      ExpressionParser expressionParser = new SpelExpressionParser();
      Expression expression = expressionParser.parseExpression("T(java.lang.Math).random() * 100");
      Double result = expression.getValue(Double.class);
      log.info("result: {}", result);
    }

    @Test
    void systemProperties() {
      ExpressionParser expressionParser = new SpelExpressionParser();
      Expression expression = expressionParser.parseExpression("'abc'.substring(2)");
      String result = expression.getValue(String.class);
      assertThat(result).isEqualTo("c");
    }

    @Test
    void templating() {
      ExpressionParser expressionParser = new SpelExpressionParser();
      Expression expression = expressionParser.parseExpression("random number is #{T(java.lang.Math).random()}",
          new TemplateParserContext());
      String result = expression.getValue(String.class);
      log.info("result: {}", result);
    }

  }

  @Nested
  class SimpleEvaluationContextTest {

    @Test
    void instanceMethod() {
      // given
      List<String> words = Arrays.asList("a", "b", "c");

      // when
      ExpressionParser expressionParser = new SpelExpressionParser();
      Expression expression = expressionParser.parseExpression("#words.size()");

      EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding()
          .withInstanceMethods()
          .build();
      context.setVariable("words", words);

      Integer result = expression.getValue(context, Integer.class);

      // then
      assertThat(result).isEqualTo(3);
    }

    @Test
    void collection() {
      // given
      List<String> words = Arrays.asList("a", "b", "c");

      // when
      ExpressionParser expressionParser = new SpelExpressionParser();
      Expression expression = expressionParser.parseExpression("#words[1]");

      EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding()
//        .withInstanceMethods()
          .build();
      context.setVariable("words", words);

      String result = expression.getValue(context, String.class);

      // then
      assertThat(result).isEqualTo("b");
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
      context.setVariable("user", user);

      String result = expression.getValue(context, String.class);

      // then
      assertThat(result).isEqualTo("1234");
    }

  }


  @Nested
  class StandardEvaluationContextTest {

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
