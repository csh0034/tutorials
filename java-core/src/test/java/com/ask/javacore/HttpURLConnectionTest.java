package com.ask.javacore;

import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.Test;

class HttpURLConnectionTest {

  @Test
  void test() throws Exception {
    URL url = new URL("https://naver.com");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//    connection.setChunkedStreamingMode(0);
    connection.setDoOutput(true);

    System.out.println("===============REQUEST===================");
    connection.getRequestProperties().forEach((k, v) -> System.out.println(k + " : " + v));

    System.out.println("===============RESPONSE===================");
    connection.getHeaderFields().forEach((k, v) -> System.out.println(k + " : " + v));
  }

}
