package com.ask.springcore.dateformat.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindException;

@WebMvcTest(DateFormatController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class DateFormatControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("GET DateTimeFormat Binding")
  @Test
  void method1() throws Exception {
    // given
    String name = "ASk1";
    String requestDateTime = "2022-02-08T01:00:00";

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    request.add("name", name);
    request.add("requestDateTime", requestDateTime);

    // when
    ResultActions resultActions = mockMvc.perform(get("/date-format/1")
        .queryParams(request));

    // then
    resultActions.andExpectAll(
        status().isOk(),
        jsonPath("$.name").value(name),
        jsonPath("$.requestDateTime").value(requestDateTime)
    );
  }

  @DisplayName("GET JsonFormat Binding, Exception")
  @Test
  void method2() throws Exception {
    // given
    String name = "ASk2";
    String requestDateTime = "2022-02-08T02:00:00";

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    request.add("name", name);
    request.add("requestDateTime", requestDateTime);

    // when
    ResultActions resultActions = mockMvc.perform(get("/date-format/2")
        .queryParams(request));

    // then
    resultActions.andExpectAll(
        status().isBadRequest(),
        result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class)
    );
  }

  @DisplayName("POST @ModelAttribute DateTimeFormat Binding")
  @Test
  void method3() throws Exception {
    // given
    String name = "ASk3";
    String requestDateTime = "2022-02-08T03:00:00";

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    request.add("name", name);
    request.add("requestDateTime", requestDateTime);

    // when
    ResultActions resultActions = mockMvc.perform(post("/date-format/3")
        .params(request));

    // then
    resultActions.andExpectAll(
        status().isOk(),
        jsonPath("$.name").value(name),
        jsonPath("$.requestDateTime").value(requestDateTime)
    );
  }

  @DisplayName("POST @ModelAttribute JsonFormat Binding, Exception")
  @Test
  void method4() throws Exception {
    // given
    String name = "ASk4";
    String requestDateTime = "2022-02-08T04:00:00";

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    request.add("name", name);
    request.add("requestDateTime", requestDateTime);

    // when
    ResultActions resultActions = mockMvc.perform(post("/date-format/4")
        .params(request));

    // then
    resultActions.andExpectAll(
        status().isBadRequest(),
        result -> assertThat(result.getResolvedException()).isInstanceOf(BindException.class)
    );
  }

  @DisplayName("POST @RequestBody DateTimeFormat Binding")
  @Test
  void method5() throws Exception {
    // given
    String name = "ASk5";
    String requestDateTime = "2022-02-08T05:00:00";

    Map<String, String> request = new HashMap<>();
    request.put("name", name);
    request.put("requestDateTime", requestDateTime);

    // when
    ResultActions resultActions = mockMvc.perform(post("/date-format/5")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    resultActions.andExpectAll(
        status().isOk(),
        jsonPath("$.name").value(name),
        jsonPath("$.requestDateTime").value(requestDateTime)
    );
  }

  @DisplayName("POST @RequestBody method JsonFormat Binding")
  @Test
  void method6() throws Exception {
    // given
    String name = "ASk6";
    String requestDateTime = "2022-02-08T06:00:00";

    Map<String, String> request = new HashMap<>();
    request.put("name", name);
    request.put("requestDateTime", requestDateTime);

    // when
    ResultActions resultActions = mockMvc.perform(post("/date-format/6")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    resultActions.andExpectAll(
        status().isOk(),
        jsonPath("$.name").value(name),
        jsonPath("$.requestDateTime").value(requestDateTime)
    );
  }

}
