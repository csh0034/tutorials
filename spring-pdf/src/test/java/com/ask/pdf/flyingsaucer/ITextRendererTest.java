package com.ask.pdf.flyingsaucer;

import com.lowagie.text.pdf.BaseFont;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

class ITextRendererTest {

  @Test
  void pdf() throws Exception {
    try (OutputStream outputStream = new FileOutputStream("target/flyingsaucerHelloWorld.pdf")) {
      ITextRenderer renderer = new ITextRenderer();

      renderer.getFontResolver().addFont(
          new ClassPathResource("font/NanumBarunGothic.ttf").getURL().toString(),
          BaseFont.IDENTITY_H,
          BaseFont.EMBEDDED
      );

      ClassPathResource resource = new ClassPathResource("templates/sample.html");
      String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

      renderer.setDocumentFromString(html);
      renderer.layout();
      renderer.createPDF(outputStream);
    }
  }

}
