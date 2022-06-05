package com.ask.apachepoi.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
class ExcelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void download() throws Exception {
    // given
    int rows = 20;

    // when
    ResultActions result = mockMvc.perform(get("/download")
        .queryParam("rows", String.valueOf(rows)));

    // then
    result.andExpectAll(status().isOk(),
        header().exists(HttpHeaders.CONTENT_LENGTH),
        header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE),
        header().string(HttpHeaders.CONTENT_DISPOSITION, Matchers.containsString("sample.xlsx"))
    );
  }

}
