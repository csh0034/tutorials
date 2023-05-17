package com.ask.javacore.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class JsonPathTest {

  @Test
  void json() {
    String json = "{\"type\":\"save\",\"body\":{\"message\":\"data....\"}}";

    DocumentContext context = JsonPath.using(Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS))
        .parse(json);

    Set<String> read = context.read("$.keys()");
    System.out.println("read = " + read);

    String type = context.read("$.type", String.class);
    assertThat(type).isEqualTo("save");

    Map<String, String> body = context.read("$.body");
    assertThat(body).containsKey("message");
  }

}
