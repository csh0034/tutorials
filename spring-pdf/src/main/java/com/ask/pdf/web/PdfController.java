package com.ask.pdf.web;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
@RequiredArgsConstructor
public class PdfController {

  private final TemplateEngine templateEngine;

  @GetMapping("/pdf-inline")
  public ResponseEntity<Resource> pdfInline(@RequestParam(defaultValue = "10") int size) {
    Resource pdf = generatePdf(size);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
            .filename("sample.pdf")
            .build()
            .toString())
        .body(pdf);
  }

  @GetMapping("/pdf-attachment")
  public ResponseEntity<Resource> pdfAttachment(@RequestParam(defaultValue = "10") int size) {
    Resource pdf = generatePdf(size);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
            .filename("sample.pdf")
            .build()
            .toString())
        .body(pdf);
  }

  private Resource generatePdf(int size) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.toStream(os);
      builder.useFont(new ClassPathResource("font/NanumGothic.ttf").getFile(), "NanumGothic");

      Map<String, Object> variables = new HashMap<>();
      variables.put("numbers", IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()));

      String html = templateEngine.process("/hello", new Context(Locale.getDefault(), variables));

      builder.withHtmlContent(html, "/");
      builder.run();
      return new ByteArrayResource(os.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
