package com.ask.springcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ObjectMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void jsonUnwrapped() throws Exception {
    // given
    Parent parent = new Parent();
    parent.age = 30;
    parent.name = new Name("John", "Mayer");

    // when
    String json = objectMapper.writeValueAsString(parent);

    // then
    log.info("json: {}", json);

    DocumentContext context = JsonPath.parse(json);
    assertAll(
        () -> assertThat(context.read("$.age", Integer.class)).isEqualTo(30),
        () -> assertThat(context.read("$.first", String.class)).isEqualTo("John"),
        () -> assertThat(context.read("$.last", String.class)).isEqualTo("Mayer")
    );
  }

  @Test
  void jsonNaming() throws Exception {
    // given
    Sms sms = new Sms();
    sms.setMT_NODE_NO("123");
    sms.setSG_CRT_SNO("456");

    // when
    String json = objectMapper.writeValueAsString(sms);

    // then
    log.info("json: {}", json);

    DocumentContext context = JsonPath.parse(json);
    assertAll(
        () -> assertThat(context.read("$.SG_CRT_SNO", String.class)).isEqualTo(sms.getSG_CRT_SNO()),
        () -> assertThat(context.read("$.MT_NODE_NO", String.class)).isEqualTo(sms.getMT_NODE_NO())
    );
  }

  public static class Parent {

    public int age;

    @JsonUnwrapped
    public Name name;

  }

  @AllArgsConstructor
  public static class Name {

    public String first;
    public String last;

  }

  @JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
  @Getter
  @Setter
  public static class Sms {

    private String SG_CRT_SNO = "";
    private String MT_NODE_NO = "";

  }

}
