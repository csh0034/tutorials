package com.ask.pdf.web;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfController {

  @GetMapping("/pdf-inline")
  public ResponseEntity<Resource> pdfInline() {
    Resource pdf = generatePdf();

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
            .filename("sample.pdf")
            .build()
            .toString())
        .body(pdf);
  }

  @GetMapping("/pdf-attachment")
  public ResponseEntity<Resource> pdfAttachment() {
    Resource pdf = generatePdf();

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
            .filename("sample.pdf")
            .build()
            .toString())
        .body(pdf);
  }

  private Resource generatePdf() {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.toStream(os);
      builder.useFont(new ClassPathResource("font/NanumBarunGothic.ttf").getFile(), "NanumBarunGothic");

      ClassPathResource resource = new ClassPathResource("templates/sample.html");
      String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
      builder.withHtmlContent(html, "/");

      builder.run();
      return new ByteArrayResource(os.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
