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
  private static final String DEFAULT_LANGUAGE = "ko";
  private static final String RESOURCES_PATH = "src/main/resources/message";

  public static void sheetsToResources(String sheetsId) throws IOException, GeneralSecurityException {
    Sheets sheets = loadSheets();
    List<String> titles = extractSheetTitles(sheets, sheetsId);

    for (String title : titles) {
      ValueRange response = sheets.spreadsheets().values()
          .get(sheetsId, title)
          .execute();

      generateResources(title, response);
    }
  }

  private static Sheets loadSheets() throws IOException, GeneralSecurityException {
    HttpRequestInitializer credential = loadCredentials();
    return new Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  private static HttpRequestInitializer loadCredentials() throws IOException {
    InputStream is = new ClassPathResource(SERVICE_ACCOUNT_JSON).getInputStream();
    GoogleCredentials googleCredentials = ServiceAccountCredentials.fromStream(is);
    return new HttpCredentialsAdapter(googleCredentials);
  }

  private static List<String> extractSheetTitles(Sheets sheets, String sheetsId) throws IOException {
    Spreadsheet spreadsheet = sheets.spreadsheets().get(sheetsId).execute();
    return spreadsheet.getSheets()
        .stream()
        .map(Sheet::getProperties)
        .map(SheetProperties::getTitle)
        .collect(toList());
  }

  private static void generateResources(String title, ValueRange response) {
    List<SheetsVO> sheetsVOs = extractHead(response);
    putSheetsBodyToVOs(response, sheetsVOs);

    for (SheetsVO sheetsVO : sheetsVOs) {
      generatePropertiesFile(title, sheetsVO, sheetsVO.getLanguage());
    }

    generatePropertiesFile(title, getDefaultLanguageVO(sheetsVOs));
  }

  private static List<SheetsVO> extractHead(ValueRange response) {
    List<SheetsVO> headVOs = new ArrayList<>();

    List<Object> objects = response.getValues().get(0);
    for (int i = 1; i < objects.size(); i++) {
      SheetsVO sheetsVO = new SheetsVO(String.valueOf(objects.get(i)));
      headVOs.add(sheetsVO);
    }

    return headVOs;
  }

  private static void putSheetsBodyToVOs(ValueRange response, List<SheetsVO> sheetsVOs) {
    List<List<Object>> values = response.getValues();

    for (int i = 1; i < values.size(); i++) {
      List<Object> objects = values.get(i);
      for (int j = 1; j < objects.size(); j++) {
        Map<String, String> data = sheetsVOs.get(j - 1).getData();

        String key = String.valueOf(objects.get(0));
        String value = String.valueOf(objects.get(j));

        data.put(key, value);
      }
    }
  }

  private static void generatePropertiesFile(String title, SheetsVO sheetsVO) {
    generatePropertiesFile(title, sheetsVO, "");
  }

  private static void generatePropertiesFile(String title, SheetsVO sheetsVO, String language) {
    String propertiesPath = getPropertiesPath(title, language);

    try (OutputStream outputStream = Files.newOutputStream(Paths.get(propertiesPath))) {
      for (Map.Entry<String, String> entry : sheetsVO.getData().entrySet()) {
        outputStream.write(String.format("%s=%s\n", entry.getKey(), entry.getValue()).getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getPropertiesPath(String title, String language) {
    if (StringUtils.isBlank(language)) {
      return String.format("%s/%s.properties", RESOURCES_PATH, title);
    }
    return String.format("%s/%s_%s.properties", RESOURCES_PATH, title, language);
  }

  private static SheetsVO getDefaultLanguageVO(List<SheetsVO> sheetsVOs) {
    return sheetsVOs.stream()
        .filter(vo -> StringUtils.equals(vo.getLanguage(), DEFAULT_LANGUAGE))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("default language sheet does not exist"));
  }

}
