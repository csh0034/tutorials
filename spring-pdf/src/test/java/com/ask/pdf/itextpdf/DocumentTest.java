package com.ask.pdf.itextpdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import org.junit.jupiter.api.Test;

class DocumentTest {

  @Test
  void pdf() throws Exception {
    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream("target/iTextHelloWorld.pdf"));

    document.open();
    Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    Chunk chunk = new Chunk("Hello World", font);

    document.add(chunk);
    document.close();
  }

}
