package com.ask.springcore.session;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class SessionControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private HttpServletRequest request;

  @Test
  void saveSession() throws Exception {
    // given
    String key = SessionController.TEST_SESSION_KEY;
    log.info("session id : {}", request.getSession().getId());

    // when
    ResultActions result = mvc.perform(get("/save/session")
        .session((MockHttpSession) request.getSession()));


    // then
    MvcResult mvcResult = result.andDo(print())
        .andReturn();

    MockHttpServletRequest request = mvcResult.getRequest();
    HttpSession session = request.getSession();

    assert session != null;
    log.info("session id : {}", session.getId());
    log.info("session id : {}", session);
    log.info("{} : {}", key, session.getAttribute(key));
  }
}