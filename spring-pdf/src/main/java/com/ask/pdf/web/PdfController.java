package com.ask.pdf.web;

import com.ask.pdf.config.PdfGenerator;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PdfController {

  private final PdfGenerator pdfGenerator;

  @GetMapping("/pdf-inline")
  public ResponseEntity<Resource> pdfInline(@RequestParam(defaultValue = "10") int size) {
    Map<String, Object> variables = Map.of("numbers", IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()));
    Resource pdf = pdfGenerator.generate("/hello", variables, Locale.getDefault());

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
    Map<String, Object> variables = Map.of("numbers", IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()));
    Resource pdf = pdfGenerator.generate("/hello", variables, Locale.getDefault());

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
            .filename("sample.pdf")
            .build()
            .toString())
        .body(pdf);
  }

  @GetMapping("/page")
  public ResponseEntity<Resource> pdfPage() {
    Resource pdf = pdfGenerator.generate("/page", Collections.emptyMap(), Locale.getDefault());

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
            .filename("page.pdf")
            .build()
            .toString())
        .body(pdf);
  }

}
