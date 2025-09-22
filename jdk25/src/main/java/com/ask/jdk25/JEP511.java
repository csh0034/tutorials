// JEP 511: Module Import Declarations
package com.ask.jdk25;

import module java.base;

//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.Date;

public class JEP511 {
  static void main() {
    var date = new Date();
    var now = LocalDateTime.now();
    var path = Paths.get("/tmp");

    System.out.println(date);
    System.out.println(now);
    System.out.println(path);
  }
}
