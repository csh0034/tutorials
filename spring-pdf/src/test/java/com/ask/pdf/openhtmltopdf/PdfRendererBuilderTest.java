package com.ask.pdf.openhtmltopdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
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
      builder.useFont(new ClassPathResource("font/NotoSansCJKkr-Regular.ttf").getFile(), "cjk");
      builder.useFont(new ClassPathResource("font/NotoEmoji-Regular.ttf").getFile(), "emoji");

      ClassPathResource resource = new ClassPathResource("templates/sample.html");
      String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
      builder.withW3cDocument(new W3CDom().fromJsoup(Jsoup.parse(html)), "/");
      builder.run();

//      content = os.toByteArray();
    }

//    assertThat(content).isNotEmpty();
  }

}
