package com.ask.sheets.google;

import org.junit.jupiter.api.Test;

class SheetsUtilsTest {

  private static final String LANGUAGE_RESOURCE_FILE_ID = "14lqXlxB1RFfVKHK2YHPonAG5GAIlyJ9sxQ-rlubELwY";

  @Test
  void sheetsToResources() throws Exception {
    SheetsUtils.sheetsToResources(LANGUAGE_RESOURCE_FILE_ID);
  }

}
