package com.ask.javacore.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class ZipTest {

  @Test
  void zip() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ZipArchiveOutputStream zos = new ZipArchiveOutputStream(bos);

    ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
    zos.putArchiveEntry(entry);
    zos.write("test..".getBytes(StandardCharsets.UTF_8));
    zos.closeArchiveEntry();

    zos.close();
    bos.close();

    FileUtils.writeByteArrayToFile(new File(System.getProperty("user.home") + "/Downloads/test.zip"), bos.toByteArray());
  }

}
