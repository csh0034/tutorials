package com.ask.springcore.multipart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

  @PostMapping("/upload")
  public String upload(@RequestPart("uploadRequest") UploadRequest uploadRequest, @RequestPart("file") MultipartFile file) {
    log.info("uploadRequest: {}, file: {}", uploadRequest, file);
    return "upload";
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @ToString
  public static class UploadRequest {

    private String id;
    private String name;

  }

}
