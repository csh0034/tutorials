package com.ask.pdf.openhtmltopdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

class PdfRendererBuilderTest {

  @Test
  void pdf() throws Exception {

//    byte[] content;
//    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

    try (OutputStream os = new FileOutputStream("target/openhtmltopdfHelloWorld.pdf")) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.toStream(os);
//      builder.useFont(new ClassPathResource("font/NanumGothic.ttf").getFile(), "NanumGothic");
      builder.useFont(new ClassPathResource("font/NotoSansCJKtc-Regular.ttf").getFile(), "cjk");

      ClassPathResource resource = new ClassPathResource("templates/sample.html");
      String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
      builder.withHtmlContent(html, "/");

      builder.run();

//      content = os.toByteArray();
    }

//    assertThat(content).isNotEmpty();
  }

}
