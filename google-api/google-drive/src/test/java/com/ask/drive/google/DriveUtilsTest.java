package com.ask.drive.google;

import org.junit.jupiter.api.Test;

class DriveUtilsTest {

  private static final String GOOGLE_DRIVE_FILE_ID = "14lqXlxB1RFfVKHK2YHPonAG5GAIlyJ9sxQ-rlubELwY";

  @Test
  void downloadToExcel() throws Exception {
    DriveUtils.downloadToExcel(GOOGLE_DRIVE_FILE_ID, "src/test/resources/sample.xlsx");
  }

}
