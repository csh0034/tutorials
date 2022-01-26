package com.ask.javacore.file;

import com.ask.javacore.common.BaseTest;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WatchServiceTest extends BaseTest {

  @DisplayName("15초안에 경로에 지정된 파일이 생성되는지 확인")
  @Test
  void watch() {
    try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
      String downloadDirectory = System.getProperty("user.home") + File.separator + "Downloads";
      String targetFilename = "watch";

      Path path = Paths.get(downloadDirectory);
      path.register(watchService,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE,
          StandardWatchEventKinds.ENTRY_MODIFY);

      WatchKey key;

      while ((key = watchService.poll(15, TimeUnit.SECONDS)) != null) {
        for (WatchEvent<?> event : key.pollEvents()) {
          print("event:  " + event.kind());
          print("file : " + event.context());
          if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)
              && StringUtils.endsWith(event.context().toString(), targetFilename)) {
            return;
          }
        }
        key.reset();
      }

    } catch(IOException | InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }
}
