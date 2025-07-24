package com.ask.javacore.watch;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WatchServiceMain {

  public static void main(String[] args) {
    String dirPath = "test-watch-dir";
    int timeoutSeconds = 200;

    try {
      Files.createDirectories(Paths.get(dirPath));
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
      Path path = Paths.get(dirPath);
      path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

      log("Watching directory: " + dirPath + " (timeout " + timeoutSeconds + " seconds)");

      new Thread(() -> {
        try {
          TimeUnit.SECONDS.sleep(5);
          Path newFile = path.resolve("test.txt");
          Files.write(newFile, "Hello".getBytes());
          log("File created: " + newFile);
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
      }).start();

      WatchKey key;
      while ((key = watchService.poll(timeoutSeconds, TimeUnit.SECONDS)) != null) {
        for (WatchEvent<?> event : key.pollEvents()) {
          Kind<?> kind = event.kind();

          if (kind == StandardWatchEventKinds.OVERFLOW) {
            log("Overflow event ignored.");
            continue;
          }

          Object context = event.context();
          if (!(context instanceof Path)) {
            log("Invalid event context, skipping.");
            continue;
          }

          Path fileName = (Path) context;
          log("Event kind: " + kind.name() + ", File: " + fileName);

          if ("test.txt".equals(fileName.toString())) {
            log("Detected target file, exiting.");
            return;
          }
        }
        key.reset();
      }

      log("Timeout reached without detecting target file.");

    } catch (IOException | InterruptedException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private static void log(String message) {
    String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    System.out.println("[" + time + "] " + message);
  }
}

