package com.ask.sheets.google;

import static java.util.stream.Collectors.toList;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Builder;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SheetsReadUtils {

  private static final String APPLICATION_NAME = "google-sheet";
  private static final String SERVICE_ACCOUNT_JSON = "service.json";
  private static final HttpTransport HTTP_TRANSPORT;

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Sheets readSheets() {
    Credential credential = getCredentials();
    return new Builder(HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  private static Credential getCredentials() {
    try {
      InputStream is = new ClassPathResource(SERVICE_ACCOUNT_JSON).getInputStream();
      return GoogleCredential.fromStream(is).createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY));
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

}
