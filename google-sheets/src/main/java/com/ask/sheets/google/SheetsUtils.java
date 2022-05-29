package com.ask.sheets.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SheetsUtils {

  private static final String COMMENT = "언어리소스";
  private static final String DEFAULT_LANGUAGE = "en";
  private static final String TARGET_DIR_PATH = "src/main/resources/message";

  public static void sheetToResources(String spreadsheetId) throws IOException {
    Sheets service = SheetsReadUtils.readSheets();
    List<String> sheetTitles = SheetsReadUtils.getSheetTitles(service, spreadsheetId);

    for (String sheetTitle : sheetTitles) {
      ValueRange response = service.spreadsheets().values()
          .get(spreadsheetId, sheetTitle)
          .execute();

      generateResources(sheetTitle, response);
    }
  }

  private static void generateResources(String title, ValueRange response) {
    List<SheetsDataVO> sheetsDataVOS = new ArrayList<>();
    titleSheetDataVO(sheetsDataVOS, response);
    getSheetDataVO(sheetsDataVOS, response);

    for (SheetsDataVO sheetsDataVO : sheetsDataVOS) {
      createPropertiesFile(title, sheetsDataVO, sheetsDataVO.getLanguage());
    }
    SheetsDataVO defaultSheetsDataVO = getDefaultSheetDataVO(sheetsDataVOS);
    createPropertiesFile(title, defaultSheetsDataVO, "");
  }

  private static void titleSheetDataVO(List<SheetsDataVO> sheetsDataVOS, ValueRange response) {
    List<List<Object>> values = response.getValues();
    List<Object> objects = values.get(0);
    for (int i = 1; i < objects.size(); i++) {
      SheetsDataVO dataVO = new SheetsDataVO();
      dataVO.setLanguage(String.valueOf(objects.get(i)));
      sheetsDataVOS.add(dataVO);
    }
  }

  private static void getSheetDataVO(List<SheetsDataVO> sheetsDataVOS, ValueRange response) {
    List<List<Object>> values = response.getValues();

    for (int i = 0; i < values.size(); i++) {

      if (i == 0) {
        continue;
      }

      String key = "";
      List<Object> rowData = values.get(i);
      for (int j = 0; j < rowData.size(); j++) {
        if (j == 0) {
          key = rowData.get(j).toString();
        } else {
          sheetsDataVOS.get(j - 1).getDataMap().put(key, rowData.get(j).toString());
        }
      }

    }
  }

  private static void createPropertiesFile(String sheetName, SheetsDataVO sheetsDataVO, String targetLanguage) {
    String resourceFilePath = getResourceFilePath(sheetName, targetLanguage);

    Properties properties = new Properties();

    if (sheetsDataVO != null) {
      for (Map.Entry<String, String> entry : sheetsDataVO.getDataMap().entrySet()) {
        properties.setProperty(entry.getKey(), entry.getValue());
      }
    }

    try {
      properties.store(new OutputStreamWriter(Files.newOutputStream(Paths.get(resourceFilePath)), StandardCharsets.UTF_8),
          COMMENT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getResourceFilePath(String sheetName, String targetLanguage) {
    if (StringUtils.isBlank(targetLanguage)) {
      return String.format("%s/%s.properties", TARGET_DIR_PATH, sheetName);
    } else {
      return String.format("%s/%s_%s.properties", TARGET_DIR_PATH, sheetName, targetLanguage);
    }
  }

  private static SheetsDataVO getDefaultSheetDataVO(List<SheetsDataVO> sheetsDataVOS) {
    for (SheetsDataVO vo : sheetsDataVOS) {
      if (DEFAULT_LANGUAGE.equals(vo.getLanguage())) {
        return vo;
      }
    }
    return null;
  }

}
