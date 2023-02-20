package com.ask.springcore.redirect;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RedirectController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class RedirectControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void redirect() throws Exception {
    mockMvc.perform(get("/redirect"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/redirected"));
  }

  @Test
  void redirectWithResponseEntity() throws Exception {
    mockMvc.perform(get("/redirect2"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/redirected"));
  }

  @Test
  void notFound() throws Exception {
    mockMvc.perform(get("/notFound"))
        .andExpect(status().isNotFound());
  }

}
