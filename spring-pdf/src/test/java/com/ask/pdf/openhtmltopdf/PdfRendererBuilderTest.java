package com.ask.pdf.openhtmltopdf;

import static org.assertj.core.api.Assertions.assertThat;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

class PdfRendererBuilderTest {

  @Test
  void pdf() throws Exception {
//    try (OutputStream os = new FileOutputStream("target/openhtmltopdfHelloWorld.pdf")) {

    byte[] content;

    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.toStream(os);
      builder.useFont(new ClassPathResource("font/NanumBarunGothic.ttf").getFile(), "NanumBarunGothic");

      ClassPathResource resource = new ClassPathResource("templates/sample.html");
      String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
      builder.withHtmlContent(html, "/");

      builder.run();

      content = os.toByteArray();
    }

    assertThat(content).isNotEmpty();
  }

}
