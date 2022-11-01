package com.ask.springrest.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void root() throws Exception {
    // when
    ResultActions result = mockMvc.perform(get("/products"));

    // then
    result.andExpectAll(
        status().isOk(),
        header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)
    );
  }

  @Test
  void find() throws Exception {
    // given
    String productId = "product01";

    // when
    ResultActions result = mockMvc.perform(get("/products/{id}", productId));

    // then
    result.andExpectAll(
        status().isOk(),
        header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)
    );
  }

}
