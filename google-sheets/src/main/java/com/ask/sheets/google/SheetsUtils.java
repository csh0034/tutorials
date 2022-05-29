package com.ask.sheets.google;

import static java.util.stream.Collectors.toList;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Builder;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SheetsUtils {

  private static final String APPLICATION_NAME = "google-sheet";
  private static final String SERVICE_ACCOUNT_JSON = "service.json";
  private static final String DEFAULT_LANGUAGE = "en";
  private static final String TARGET_DIR_PATH = "src/main/resources/message";

  public static void sheetToResources(String spreadsheetId) throws IOException {
    Sheets service = readSheets();
    List<String> sheetTitles = getSheetTitles(service, spreadsheetId);

    for (String sheetTitle : sheetTitles) {
      ValueRange response = service.spreadsheets().values()
          .get(spreadsheetId, sheetTitle)
          .execute();

      generateResources(sheetTitle, response);
    }
  }

  public static Sheets readSheets() {
    HttpRequestInitializer credential = getCredentials();
    try {
      return new Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
          .setApplicationName(APPLICATION_NAME)
          .build();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static HttpRequestInitializer getCredentials() {
    try {
      InputStream is = new ClassPathResource(SERVICE_ACCOUNT_JSON).getInputStream();
      GoogleCredentials googleCredentials = ServiceAccountCredentials.fromStream(is);
      return new HttpCredentialsAdapter(googleCredentials);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> getSheetTitles(Sheets service, String spreadsheetId) throws IOException {
    Spreadsheet spreadsheet = service.spreadsheets().get(spreadsheetId).execute();
    List<Sheet> sheets = spreadsheet.getSheets();
    return sheets.stream()
        .map(Sheet::getProperties)
        .map(SheetProperties::getTitle)
        .collect(toList());
  }

  private static void generateResources(String title, ValueRange response) {
    List<SheetsDataVO> sheetsDataVOS = new ArrayList<>();
    titleSheetDataVO(sheetsDataVOS, response);
    getSheetDataVO(sheetsDataVOS, response);

    for (SheetsDataVO sheetsDataVO : sheetsDataVOS) {
      createPropertiesFile(title, sheetsDataVO, sheetsDataVO.getLanguage());
    }

    createPropertiesFile(title, getDefaultSheetDataVO(sheetsDataVOS));
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

  private static void createPropertiesFile(String sheetName, SheetsDataVO sheetsDataVO) {
    createPropertiesFile(sheetName, sheetsDataVO, "");
  }

  private static void createPropertiesFile(String sheetName, SheetsDataVO sheetsDataVO, String targetLanguage) {
    String resourceFilePath = getResourceFilePath(sheetName, targetLanguage);

    try (OutputStream outputStream = Files.newOutputStream(Paths.get(resourceFilePath))) {
      for (Map.Entry<String, String> entry : sheetsDataVO.getDataMap().entrySet()) {
        outputStream.write(String.format("%s=%s\n", entry.getKey(), entry.getValue()).getBytes(StandardCharsets.UTF_8));
      }
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
    return sheetsDataVOS.stream()
        .filter(vo -> StringUtils.equals(vo.getLanguage(), DEFAULT_LANGUAGE))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("default language sheet does not exist"));
  }

}
