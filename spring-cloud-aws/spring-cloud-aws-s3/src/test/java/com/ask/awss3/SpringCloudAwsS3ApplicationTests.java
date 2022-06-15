package com.ask.awss3;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringCloudAwsS3ApplicationTests {

  @Autowired
  private AmazonS3 amazonS3;

  @Autowired
  private ResourceLoader resourceLoader;

  @Order(1)
  @Test
  void delete() {
    amazonS3.deleteObject("my-bucket", "test.log");
  }

  @Order(2)
  @Test
  void upload() throws IOException {
    amazonS3.createBucket("my-bucket");

    Resource resource = resourceLoader.getResource("s3://my-bucket/test.log");
    WritableResource writableResource = (WritableResource) resource;
    try (OutputStream outputStream = writableResource.getOutputStream()) {
      outputStream.write("test".getBytes(StandardCharsets.UTF_8));
    }
  }

  @Order(3)
  @Test
  void download() throws IOException {
    Resource resource = resourceLoader.getResource("s3://my-bucket/test.log");

    String result = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    assertThat(result).isEqualTo("test");
  }

}
