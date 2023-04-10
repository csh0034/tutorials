package com.ask.pdf.config;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class PdfGenerator {

  private final TemplateEngine templateEngine;

  public Resource generate(String template, Map<String, Object> variables, Locale locale) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.toStream(os);
      builder.useFont(this::getFont, "cjk");

      // classpath 기준으로 image 파일을 읽어오기 위해 baseUrl 설정
      String baseUrl = getClass()
          .getProtectionDomain()
          .getCodeSource()
          .getLocation()
          .toString();

      builder.withHtmlContent(templateEngine.process(template, new Context(locale, variables)), baseUrl);
      builder.run();
      return new ByteArrayResource(os.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private InputStream getFont() {
    try {
      return new ClassPathResource("font/NotoSansCJKtc-Regular.ttf").getInputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
