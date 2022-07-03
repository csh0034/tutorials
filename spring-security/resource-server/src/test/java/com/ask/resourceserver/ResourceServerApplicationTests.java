package com.ask.resourceserver;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceServerApplicationTests {

  private static final String NO_SCOPES_TOKEN;
  private static final String MESSAGE_READ_TOKEN;
  private static final String EXPIRED_TOKEN;

  static {
    try {
      Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("token.properties"));

      NO_SCOPES_TOKEN = properties.getProperty("noScopesToken");
      MESSAGE_READ_TOKEN = properties.getProperty("messageReadToken");
      EXPIRED_TOKEN = properties.getProperty("expiredToken");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Autowired
  private MockMvc mvc;

  @DisplayName("/ : 스코프가 없는 토큰으로 접근 가능")
  @Test
  void validTokenNoScope() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/")
        .with(bearerToken(NO_SCOPES_TOKEN)));

    // then
    result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Hello, messaging-client!")));
  }

  @DisplayName("/message : Read 토큰으로 접근 가능")
  @Test
  void validTokenReadScope() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/message")
        .with(bearerToken(MESSAGE_READ_TOKEN)));

    // then
    result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("secret message")));
  }

  @DisplayName("/message : 스코프가 없는 토큰으로 접근 불가능")
  @Test
  void invalidTokenNoScope() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/message")
        .with(bearerToken(NO_SCOPES_TOKEN)));

    // then
    result.andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, containsString("Bearer error=\"insufficient_scope\"")));
  }

  @DisplayName("만료시간이 다되었을경우 접근 불가능")
  @Test
  void invalidTokenExpired() throws Exception {
    // when
    ResultActions result = mvc.perform(get("/message")
        .with(bearerToken(EXPIRED_TOKEN)));

    // then
    result.andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, containsString("Bearer error=\"invalid_token\"")))
        .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, containsString("Jwt expired at")));
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
