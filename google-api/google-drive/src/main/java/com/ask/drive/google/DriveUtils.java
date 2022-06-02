package com.ask.drive.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriveUtils {

  private static final String APPLICATION_NAME = "google-drive";
  private static final String SERVICE_ACCOUNT_JSON = "service.json";

  public static void downloadToExcel(String fileId, String path) throws IOException, GeneralSecurityException {
    Drive drive = loadDrive();
    try (FileOutputStream outputStream = new FileOutputStream(path)) {
      drive.files().export(fileId, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
          .executeMediaAndDownloadTo(outputStream);
    } catch (GoogleJsonResponseException e) {
      throw new RuntimeException(e);
    }
  }

  private static Drive loadDrive() throws IOException, GeneralSecurityException {
    HttpRequestInitializer credential = loadCredentials();
    return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  private static HttpRequestInitializer loadCredentials() throws IOException {
    InputStream is = new ClassPathResource(SERVICE_ACCOUNT_JSON).getInputStream();
    Credentials credentials = ServiceAccountCredentials.fromStream(is).createScoped(DriveScopes.all());
    return new HttpCredentialsAdapter(credentials);
  }

}
