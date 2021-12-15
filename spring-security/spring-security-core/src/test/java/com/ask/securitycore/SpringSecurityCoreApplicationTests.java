package com.ask.securitycore;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityCoreApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void authenticate() throws Exception {
    // given
    String id = "ask";
    String password = "ask111";

    // when
    ResultActions result = mockMvc.perform(formLogin()
        .loginProcessingUrl("/sub/login")
        .user(id)
        .password(password));

    // then
    result.andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(authenticated()
            .withUsername("ask")
            .withRoles("USER"));
  }
}
