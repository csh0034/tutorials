package com.ask.multitenancy.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class TenantControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void findAllCompanies() throws Exception {
    // given

    // when
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/companies"));

    // then
    result.andDo(print());
  }
}