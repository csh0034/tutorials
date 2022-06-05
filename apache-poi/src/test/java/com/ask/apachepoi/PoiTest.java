package com.ask.apachepoi;

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

@Slf4j
class PoiTest {

  @Test
  void read() throws Exception {
    InputStream is = new ClassPathResource("sample.xlsx").getInputStream();
    XSSFWorkbook workbook = new XSSFWorkbook(is);

    XSSFSheet sheet = workbook.getSheetAt(0);
    String sheetName = sheet.getSheetName();
    log.info("sheetName: {}", sheetName);

    int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
    log.info("physicalNumberOfRows: {}", physicalNumberOfRows);

    for (int i = 0; i < physicalNumberOfRows; i++) {
      XSSFRow row = sheet.getRow(i);

      for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
        XSSFCell cell = row.getCell(j);
        String cellValue = cell.getStringCellValue();

        log.info("[i: {}, j: {}] {}", i, j, cellValue);
      }
    }
  }

}
