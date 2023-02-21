package com.ask.tiles.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class LayoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void home() throws Exception {
    mockMvc.perform(get("/admin/home"))
        .andExpectAll(
            status().isOk(),
            forwardedUrl("/WEB-INF/tiles/layout/admin-layout.jsp")
        );
  }

}
