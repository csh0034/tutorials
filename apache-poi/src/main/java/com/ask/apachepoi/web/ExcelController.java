package com.ask.apachepoi.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

  @GetMapping("/download")
  public ResponseEntity<Resource> download(int rows) {
    try (Workbook workbook = new XSSFWorkbook()) {

      Sheet sheet = workbook.createSheet("users");

      Font font = workbook.createFont();
      font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
      font.setFontHeightInPoints((short) 13);

      CellStyle headStyle = workbook.createCellStyle();
      headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
      headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headStyle.setFont(font);

      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("id");
      headerRow.setHeight((short) (20 * 20));
      headerRow.getCell(0).setCellStyle(headStyle);

      for (int i = 1; i <= rows; i++) {
        Row row = sheet.createRow(i);

        row.createCell(0).setCellValue(UUID.randomUUID().toString());
      }

      sheet.setColumnWidth(0, 40 * 256);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);

      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
              .filename("sample.xlsx")
              .build()
              .toString())
          .body(new ByteArrayResource(outputStream.toByteArray()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
