package com.ask.googleapiclient.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GoogleUtils {

  public static GoogleProfile googleProfile(String clientId, String accessToken) {
    GoogleIdTokenVerifier verifier = loadVerifier(clientId);

    GoogleIdToken idToken;

    try {
      idToken = verifier.verify(accessToken);
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }

    return GoogleProfile.from(idToken.getPayload());
  }

  private static GoogleIdTokenVerifier loadVerifier(String clientId) {
    try {
      return new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
          .setAudience(Collections.singletonList(clientId))
          .build();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Data
  public static class GoogleProfile {

    private String userId;
    private String email;
    private String name;

    public static GoogleProfile from(GoogleIdToken.Payload payload) {
      GoogleProfile googleProfile = new GoogleProfile();
      googleProfile.userId = payload.getSubject();
      googleProfile.email = payload.getEmail();
      googleProfile.name = (String) payload.get("name");
      return googleProfile;
    }

  }

}
