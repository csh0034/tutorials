package com.ask.springvalidator.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.ask.springvalidator.vo.ValidatorVO1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ValidatorController.class)
class ValidatorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void validator1() throws Exception {
    // given
    ValidatorVO1 validatorVO1 = new ValidatorVO1("", 5);

    // when
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/validator1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validatorVO1)));

    // then
    result.andDo(print());
  }
}