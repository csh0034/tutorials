package com.ask.resourceserver;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:token.properties")
class ResourceServerApplicationTests {

  @Value("${noScopesToken}")
  private String noScopesToken;

  @Value("${messageReadToken}")
  private String messageReadToken;

  @Autowired
  private MockMvc mvc;

  @DisplayName("/ : 스코프가 없는 토큰으로 접근 가능하다.")
  @Test
  void validTokenNoScope() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/")
        .with(bearerToken(noScopesToken)));

    // then
    result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Hello, subject!")));
  }

  @DisplayName("/message : Read 토큰으로 접근 가능하다.")
  @Test
  void validTokenReadScope() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/message")
        .with(bearerToken(messageReadToken)));

    // then
    result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("secret message")));
  }

  @DisplayName("/message : 스코프가 없는 토큰으로 접근 불가능하다.")
  @Test
  void invalidTokenNoScope() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/message")
        .with(bearerToken(noScopesToken)));

    // then
    result.andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, containsString("Bearer error=\"insufficient_scope\"")));
  }

  private static BearerTokenRequestPostProcessor bearerToken(String token) {
    return new BearerTokenRequestPostProcessor(token);
  }

  private static class BearerTokenRequestPostProcessor implements RequestPostProcessor {

    private final String token;

    BearerTokenRequestPostProcessor(String token) {
      this.token = token;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
      request.addHeader("Authorization", "Bearer " + this.token);
      return request;
    }
  }
}
