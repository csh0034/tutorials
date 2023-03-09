package com.ask.springcore.multipart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UploadController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Slf4j
class UploadControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("RequestPart 를 사용하여 json 과 파일을 함께 mapping")
  @Test
  void upload() throws Exception {
    MockMultipartFile json = new MockMultipartFile(
        "uploadRequest",
        "uploadRequest",
        "application/json",
        "{\"id\":\"id...\",\"name\":\"name...\"}".getBytes()
    );

    MockMultipartFile file = new MockMultipartFile(
        "file",
        "test.txt",
        "text/plain",
        "Spring Framework".getBytes()
    );

    mockMvc.perform(multipart("/upload")
            .file(json)
            .file(file))
        .andExpect(status().isOk());
  }

}
