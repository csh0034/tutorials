package com.ask.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

class PDDocumentTest {

  @Test
  void pdf() throws Exception {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage();
    document.addPage(page);

    PDPageContentStream contentStream = new PDPageContentStream(document, page);

    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

    contentStream.beginText();
    contentStream.newLineAtOffset(10, 10);
    contentStream.showText("Hello PDFBox");
    contentStream.endText();
    contentStream.close();

    document.save("target/pdfBoxHelloWorld.pdf");
    document.close();
  }

}
